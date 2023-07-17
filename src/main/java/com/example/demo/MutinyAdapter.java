package com.example.demo;


import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ReactiveTypeDescriptor;
import org.springframework.stereotype.Component;



// Spring 5.3.10 adds SmallRye Mutiny support officially.
// see: https://github.com/spring-projects/spring-framework/pull/27331
//@Component
@RequiredArgsConstructor
@Slf4j
public class MutinyAdapter {
    private final ReactiveAdapterRegistry registry;

    @PostConstruct
    public void registerAdapters(){
        log.debug("registering MutinyAdapter");
//        registry.registerReactiveType(
//            ReactiveTypeDescriptor.singleOptionalValue(Uni.class, ()-> Uni.createFrom().nothing()),
//            uni ->((Uni<?>)uni).convert().toPublisher(),
//            publisher ->  Uni.createFrom().publisher(publisher)
//        );

        registry.registerReactiveType(
            ReactiveTypeDescriptor.singleOptionalValue(Uni.class, () -> Uni.createFrom().nothing()),
            uni -> (Publisher<?>) ((Uni<?>) uni).convert().toPublisher(),
            publisher -> Uni.createFrom().publisher((java.util.concurrent.Flow.Publisher<?>) publisher)
        );



        registry.registerReactiveType(
            ReactiveTypeDescriptor.multiValue(Multi.class, ()-> Multi.createFrom().empty()),
            multi -> (Publisher<?>) multi,
            publisher-> Multi.createFrom().publisher((java.util.concurrent.Flow.Publisher<?>)publisher));
    }
}
