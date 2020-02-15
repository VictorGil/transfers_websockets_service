package net.devaction.kafka.transferswebsocketsservice.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// We are aware that this class is not part of the Java API
// but we need it
import sun.misc.Signal;
import sun.misc.SignalHandler;

import net.devaction.kafka.transferswebsocketsservice.config.ConfigValues;
import net.devaction.kafka.transferswebsocketsservice.facade.BalanceAndTransferFacade;
import net.devaction.kafka.transferswebsocketsservice.facade.CadenceFacade;
import net.devaction.kafka.transferswebsocketsservice.facade.KafkaStreamsLocalStoresManager;
import net.devaction.kafka.transferswebsocketsservice.processor.AccountBalanceRequestProcessor;
import net.devaction.kafka.transferswebsocketsservice.processor.AccountBalanceRequestProcessorImpl;
import net.devaction.kafka.transferswebsocketsservice.processor.AccountBalanceSubscriptionRequestProcessor;
import net.devaction.kafka.transferswebsocketsservice.processor.AccountBalanceSubscriptionRequestProcessorImpl;
import net.devaction.kafka.transferswebsocketsservice.processor.MessageWrapperProcessor;
import net.devaction.kafka.transferswebsocketsservice.processor.MessageWrapperProcessorImpl;
import net.devaction.kafka.transferswebsocketsservice.processor.MessageWrapperProcessorSingletonProvider;
import net.devaction.kafka.transferswebsocketsservice.processor.TransferDataRequestProcessor;
import net.devaction.kafka.transferswebsocketsservice.processor.TransferDataRequestProcessorImpl;
import net.devaction.kafka.transferswebsocketsservice.processor.TransferDataSubscriptionRequestProcessor;
import net.devaction.kafka.transferswebsocketsservice.processor.TransferDataSubscriptionRequestProcessorImpl;
import net.devaction.kafka.transferswebsocketsservice.processor.dispatcher.BalanceUpdatesDispatcher;
import net.devaction.kafka.transferswebsocketsservice.processor.dispatcher.BalanceUpdatesDispatcherImpl;
import net.devaction.kafka.transferswebsocketsservice.processor.dispatcher.SessionsRemover;
import net.devaction.kafka.transferswebsocketsservice.processor.dispatcher.TransferDataDispatcher;
import net.devaction.kafka.transferswebsocketsservice.processor.dispatcher.TransferDataDispatcherImpl;
import net.devaction.kafka.transferswebsocketsservice.server.sender.AccountBalanceSender;
import net.devaction.kafka.transferswebsocketsservice.server.sender.AccountBalanceSenderImpl;
import net.devaction.kafka.transferswebsocketsservice.server.sender.MessageSender;
import net.devaction.kafka.transferswebsocketsservice.server.sender.MessageSenderImpl;
import net.devaction.kafka.transferswebsocketsservice.server.sender.TransferDataResponseSender;
import net.devaction.kafka.transferswebsocketsservice.server.sender.TransferDataUpdateSender;
import net.devaction.kafka.transferswebsocketsservice.transferscustomstore.TransfersStoreAdder;
import net.devaction.kafka.transferswebsocketsservice.transferscustomstore.TransfersStoreGetter;
import net.devaction.kafka.transferswebsocketsservice.transferscustomstore.TransfersStoreImpl;
import net.devaction.kafka.accountbalanceconsumer.AccountBalanceConsumer;
import net.devaction.kafka.accountbalanceconsumer.AccountBalanceConsumerImpl;
import net.devaction.kafka.accountbalanceconsumer.AccountBalanceUpdateProcessor;
import net.devaction.kafka.accountbalanceconsumer.AccountBalanceUpdateProcessorImpl;
import net.devaction.kafka.accountbalanceconsumer.runnable.TopicConsumerRunner;
import net.devaction.kafka.avro.AccountBalance;
import net.devaction.kafka.transferconsumer.TransferConsumer;
import net.devaction.kafka.transferconsumer.TransferConsumerImpl;
import net.devaction.kafka.transferconsumer.TransferUpdateProcessor;
import net.devaction.kafka.transferconsumer.TransferUpdateProcessorWhithCadence;
import net.devaction.kafka.transferconsumer.TransferUpdateProcessorWithoutCadence;
import net.devaction.kafka.transferswebsocketsservice.config.ConfigReader;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class WebSocketsServiceMain implements SignalHandler {

    private static final Logger log = LoggerFactory.getLogger(WebSocketsServiceMain.class);

    private static final String WINCH_SIGNAL = "WINCH";

    private BalanceAndTransferFacade balanceAndTransferFacade;
    private TransfersStoreGetter transfersStoreGetter;

    private TransfersStoreAdder transfersStoreAdder;

    private WebSocketsServer server;

    private AccountBalanceConsumer balanceConsumer;

    private TransferConsumer transferConsumer;

    public static void main(String[] args) {
        boolean usingCadence = false;

        if (args.length > 0 && "-use-cadence".equalsIgnoreCase(args[0])) {
            usingCadence = true;
        }

        new WebSocketsServiceMain().run(usingCadence);
    }

    private void run(final boolean usingCadence) {
        if (usingCadence) {
            log.info("Using Cadence");
        } else {
            log.info("Not using Cadence");
        }

        registerThisAsOsSignalHandler();

        ConfigValues configValues;
        log.info("Going to read the configuration values");
        try {
            configValues = new ConfigReader().read();
        } catch (Exception ex) {
            log.error("FATAL: Unable to read the configuration values, exiting");
            return;
        }

        if (usingCadence) {
            final String cadenceDomain = configValues.getCadenceDomain();
            log.info("The Cadence domain is \"{}\"", cadenceDomain);
            CadenceFacade cadenceFacade = new CadenceFacade(cadenceDomain);
            balanceAndTransferFacade = cadenceFacade;

            transfersStoreGetter = cadenceFacade;
        } else {
            balanceAndTransferFacade = new KafkaStreamsLocalStoresManager(configValues.getKafkaBootstrapServers(),
                    configValues.getKafkaSchemaRegistryUrl());
        }

        log.info("Going to start the Kafka local stores or alternatively, the Cadence workflow client.");
        balanceAndTransferFacade.start();
        MessageSender messageSender = new MessageSenderImpl();
        AccountBalanceSender abSender = new AccountBalanceSenderImpl(messageSender);

        AccountBalanceRequestProcessor abReqProcessor = new AccountBalanceRequestProcessorImpl(balanceAndTransferFacade, abSender);

        TransferDataResponseSender transferDataResponseSender = new TransferDataResponseSender(messageSender);

        TransferDataUpdateSender transferDataUpdateSender = new TransferDataUpdateSender(messageSender);

        TransferDataRequestProcessor tdReqProcessor = new TransferDataRequestProcessorImpl(balanceAndTransferFacade, transferDataResponseSender);

        BalanceUpdatesDispatcher balanceUpdatesDispatcher = new BalanceUpdatesDispatcherImpl(abSender);
        AccountBalanceSubscriptionRequestProcessor abSubsReqProcessor =
                new AccountBalanceSubscriptionRequestProcessorImpl(balanceUpdatesDispatcher);

        TransferDataDispatcher transferDispatcher = new TransferDataDispatcherImpl(transferDataUpdateSender);

        if (!usingCadence) {
            TransfersStoreImpl transfersStore = new TransfersStoreImpl();
            transfersStoreGetter = transfersStore;
            transfersStoreAdder = transfersStore;
        }

        TransferDataSubscriptionRequestProcessor transferSubscriptionReqProcessor =
                new TransferDataSubscriptionRequestProcessorImpl(transferDispatcher, transfersStoreGetter,
                        transferDataUpdateSender);

        // This is a singleton which is going to be used by the
        // ServerEndPoint class
        new SessionsRemover(balanceUpdatesDispatcher, transferDispatcher);

        MessageWrapperProcessor messageProcessor = new MessageWrapperProcessorImpl(
                abReqProcessor, abSubsReqProcessor, tdReqProcessor, transferSubscriptionReqProcessor);

        MessageWrapperProcessorSingletonProvider.setProcessor(messageProcessor);

        server = new WebSocketsServerImpl();
        log.info("Going to start the WebSockets server.");
        try {
            server.start(configValues.getWebsocketsServerHost(),
                    configValues.getWebsocketsServerPort(),
                    configValues.getWebsocketsContextPath());
        } catch (Exception ex) {
            log.error("Unable to start the WebSockets server, "
                    + "configuration values: {}", configValues, ex);
            stop();
        }

        AccountBalanceUpdateProcessor abUpdateProcessor = new AccountBalanceUpdateProcessorImpl(balanceUpdatesDispatcher);
        log.info("Going to start the \"accounts balance\" Kafka consumer.");
        balanceConsumer = new AccountBalanceConsumerImpl(configValues.getKafkaBootstrapServers(),
                configValues.getKafkaSchemaRegistryUrl(), abUpdateProcessor);
        // We start this runner in a new thread, otherwise it would block the execution
        // of this method
        TopicConsumerRunner<AccountBalance> balanceConsumerRunner = new TopicConsumerRunner<>(balanceConsumer);
        balanceConsumerRunner.start();

        TransferUpdateProcessor transferUpdateProcessor = null;
        if (usingCadence) {
            transferUpdateProcessor = new TransferUpdateProcessorWhithCadence(transferDispatcher);
        } else {
            transferUpdateProcessor = new TransferUpdateProcessorWithoutCadence(transferDispatcher, transfersStoreAdder);
        }

        log.info("Going to start the \"transfers\" Kafka consumer.");
        // TODO Maybe we can use the same Kafka consumer to receive messages from the two topics
        transferConsumer = new TransferConsumerImpl(configValues.getKafkaBootstrapServers(),
                configValues.getKafkaSchemaRegistryUrl(), transferUpdateProcessor);
        transferConsumer.start();
        log.info("Exiting after normal execution");
    }

    @Override
    public void handle(Signal signal) {
        log.info("We have received the signal to tell us to stop: {}", signal.getName());
        stop();
    }

    private void registerThisAsOsSignalHandler() {
        log.debug("Going to register this object to handle the {} signal", WINCH_SIGNAL);
        try {
            Signal.handle(new Signal(WINCH_SIGNAL), this);
        } catch (Exception ex) {
            // Most likely this is a signal that's not supported on this
            // platform or with the JVM as it is currently configured
            log.error("FATAL: The signal is not supported: {}, exiting", WINCH_SIGNAL, ex);
            System.exit(1);
        }
    }

    private void stop() {
        if (balanceConsumer != null) {
            balanceConsumer.stop();
        }

        if (transferConsumer != null) {
            transferConsumer.stop();
        }

        if (server != null) {
            server.stop();
        }

        if (balanceAndTransferFacade != null) {
            balanceAndTransferFacade.stop();
        }
    }
}
