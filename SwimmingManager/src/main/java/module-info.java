module SwimmingManager {
    requires java.sql;
    requires org.apache.logging.log4j;
    requires spring.context;
    requires spring.beans;
    requires spring.data.commons;

    exports model;
    exports repository;

    opens model to spring.core, spring.beans, spring.context;
    opens repository to spring.core, spring.beans, spring.context;
}
