module org.example.javragui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.sql;
    requires spring.beans;
    requires spring.context;
    requires org.aspectj.weaver;

    exports services to spring.beans;
    requires SwimmingManager;

    opens org.example.javragui to javafx.fxml;
    exports org.example.javragui;

    // gRPC and Protobuf
    requires io.grpc;
    requires io.grpc.stub;
    requires protobuf.java;
    requires java.annotation;


    // Make generated gRPC classes accessible
    opens com.example.grpc to io.grpc, javafx.base;
    exports com.example.grpc;



    requires io.grpc.protobuf;
   // requires com.google.common.util.concurrent;
    requires com.google.common;

}
