package com.github.devmix.commons.adapters;

import com.github.devmix.commons.adapters.api.AdaptersContext;
import com.github.devmix.commons.adapters.api.annotations.Adaptee;
import com.github.devmix.commons.adapters.api.annotations.Adapter;
import com.github.devmix.commons.adapters.api.annotations.DelegateRule;
import com.github.devmix.commons.adapters.api.annotations.DelegateRules;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import com.github.devmix.commons.adapters.core.contexts.AdaptersContextBuilders;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public class SimpleAdapterTest {

    public void test() throws AdapterGenerationException {
        final AdaptersContext ctx = AdaptersContextBuilders.standard()
                .addPackage(SimpleAdapterTest.class.getPackage().getName())
                .build();

        final NumberAdapter number = ctx.findAndCreateByAdaptee(Number.class);

        assert number != null;

        assertThat(number).isNotNull();
        assertThat(number.set(10L).thenPlus(5L).thenMinus(2L).get()).isEqualTo(13L);
    }

    public static final class Number {

        private Long value;

        public void initialize(final Long value) {
            this.value = value;
        }

        public void doPlus(final Long value) {
            this.value += value;
        }

        public void doMinus(final Long value) {
            this.value -= value;
        }

        public Long getValue() {
            return value;
        }
    }

    @Adapter(processing = Adapter.Processing.AUTO)
    @DelegateRules({
            @DelegateRule(from = "then(.*)", to = "do(.*)"),
            @DelegateRule(from = "get", to = "getValue"),
            @DelegateRule(from = "set", to = "initialize")
    })
    public static abstract class NumberAdapter {

        private final Number number = new Number();

        @Adaptee
        public Number adaptee() {
            return number;
        }

        public abstract NumberAdapter thenPlus(Long value);

        public abstract NumberAdapter thenMinus(Long value);

        public abstract NumberAdapter set(Long value);

        public abstract Long get();
    }
}
