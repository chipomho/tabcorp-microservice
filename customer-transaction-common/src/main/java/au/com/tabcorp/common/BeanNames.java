package au.com.tabcorp.common;

public class BeanNames {

    public static class Common{
        public static final String OBJECT_MAPPER = "tabcorpCommonObjectMapper";
    }
    public static class Data{
        public static final String CONNECTION = "tabcorpDataConnection";
        public static final String REACTIVE_TRANSACTION_MANAGER = "tabcorpDataReactiveTransactionManager";
        public static final String TRANSACTION_OPERATOR = "tabcorpDataTransactionOperator";
        public static final String R2DBC_TEMPLATE = "tabcorpDataR2dbcEntityTemplate";
        public static final String CONNECTION_FACTORY_INITIALIZER ="tabcorpDataConnectionFactoryInitializer";
        public static final String REPOSITORY_CUSTOMER = "tabcorpDataCustomerRepository";
        public static final String REPOSITORY_PRODUCT = "tabcorpDataProductRepository";
        public static final String REPOSITORY_CUSTOMER_TRANSACTION = "tabcorpDataCustomerTransactionRepository";

        public static final String SERVICE_CUSTOMER = "tabcorpDataCustomerService";
        public static final String SERVICE_CUSTOMER_TRANSACTION = "tabcorpDataCustomerTransactionService";
        public static final String SERVICE_PRODUCT = "tabcorpDataProductService";

    }

    public static class Core{
        public static final String REPORTING_FACADE = "tabcorpCoreReportingFacade";
        public static final String CUSTOMER_FACADE = "tabcorpCoreCustomerFacade";
        public static final String TRANSACTION_FACADE = "tabcorpCoreTransactionFacade";
        public static final String PRODUCT_FACADE = "tabcorpCoreProductFacade";


        public static final String CACHE_SERVICE = "tabcorpCoreCacheService";
        public static final String REDIS_TEMPLATE = "tabcorpCoreReactiveRedisTemplate";
        public static final String REDIS_CONNECTION_FACTORY = "tabcorpCoreReactiveReactiveRedisConnectionFactory";
    }

    public static class ToolkitAPI{
        public static final String VALIDATOR_TRANSACTION_MODEL = "tabcorpToolkitAPIValidatorTransactionModel";
        public static final String VALIDATOR_CUSTOMER_MODEL = "tabcorpToolkitAPIValidatorCustomerModel";
        public static final String VALIDATOR_PRODUCT_MODEL = "tabcorpToolkitAPIValidatorProductModel";
        public static final String VALIDATOR_AUTHENTICATION = "tabcorpToolkitAPIValidatorAuthenticationModel";

        public static final String DELEGATE_CUSTOMER = "tabcorpToolkitAPIDelegateCustomer";
        public static final String DELEGATE_CUSTOMER_TRANSACTION = "tabcorpToolkitAPIDelegateCustomerTransaction";
        public static final String DELEGATE_PRODUCT = "tabcorpToolkitAPIDelegateProduct";
        public static final String DELEGATE_REPORTING = "tabcorpToolkitAPIDelegateReporting";
        public static final String DELEGATE_AUTHENTICATION = "tabcorpToolkitAPIDelegateAuthentication";

        public static final String COMPONENT_AUTHENTICATION_MANAGER = "tabcorpToolkitAPIComponentAuthenticationManager";
    }

    public static class Messaging{
        public static final String RABBITMQ_CONNECTION_FACTORY = "tabcorpMessagingRabbitMQConnectionFactory";
        public static final String RABBITMQ_CLUSTER_NODE_ADDRESSES = "tabcorpMessagingRabbitClusterNodeAddresses";
        public static final String RABBITMQ_MESSAGE_SENDER = "tabcorpMessagingRabbitMessageSender";
        public static final String RABBITMQ_MESSAGE_RECEIVER = "tabcorpMessagingRabbitMessageReceiver";
    }

}
