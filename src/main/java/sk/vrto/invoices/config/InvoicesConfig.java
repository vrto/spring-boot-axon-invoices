package sk.vrto.invoices.config;

import lombok.val;
import net.sf.ehcache.CacheManager;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.EhCacheAdapter;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.spring.config.CommandHandlerSubscriber;
import org.axonframework.spring.config.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean;
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import sk.vrto.invoices.command.invoice.Invoice;

import java.util.Arrays;
import java.util.List;

@Configuration
public class InvoicesConfig {

    @Autowired
    private EventStore eventStore;

    @Autowired
    private Cache cache;

    @Autowired
    private Snapshotter snapshotter;

    @Autowired
    AggregateFactory<Invoice> invoiceAggregateFactory;

    @Bean
    public Repository<Invoice> invoiceAggregateRepository() throws Exception {
        val snapshotTriggerDefinition = new EventCountSnapshotTriggerDefinition(
                snapshotter,
                50);

        return new CachingEventSourcingRepository<>(
                invoiceAggregateFactory,
                eventStore,
                cache,
                snapshotTriggerDefinition);
    }

    @Bean
    public AggregateFactory<Invoice> invoiceAggregateFactory() {
        val springPrototypeAggregateFactory = new SpringPrototypeAggregateFactory<Invoice>();
        springPrototypeAggregateFactory.setPrototypeBeanName("invoice");

        return springPrototypeAggregateFactory;
    }

    @Bean
    @Scope("prototype")
    public Invoice invoice() {
        return new Invoice();
    }

    @Bean
    public CommandBus commandBus() {
        val commandBus = new SimpleCommandBus();
        List<BeanValidationInterceptor<CommandMessage<?>>> beanValidationInterceptors =
                Arrays.asList(new BeanValidationInterceptor<>());
        return commandBus;
    }

    @Bean
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor() {
        return new AnnotationCommandHandlerBeanPostProcessor();
    }

    @Bean
    public CommandHandlerSubscriber commandHandlerSubscriber() {
        return new CommandHandlerSubscriber();
    }

    @Bean
    public SpringAggregateSnapshotterFactoryBean springAggregateSnapshotterFactoryBean() {
        return new SpringAggregateSnapshotterFactoryBean();
    }

    @Bean
    public EhCacheAdapter ehCache(CacheManager cacheManager) {
        return new EhCacheAdapter(cacheManager.getCache("testCache"));
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        val ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setShared(true);

        return ehCacheManagerFactoryBean;
    }
}
