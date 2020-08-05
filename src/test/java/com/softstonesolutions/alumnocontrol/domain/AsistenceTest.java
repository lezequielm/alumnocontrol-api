package com.softstonesolutions.alumnocontrol.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.softstonesolutions.alumnocontrol.web.rest.TestUtil;

public class AsistenceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Asistence.class);
        Asistence asistence1 = new Asistence();
        asistence1.setId(1L);
        Asistence asistence2 = new Asistence();
        asistence2.setId(asistence1.getId());
        assertThat(asistence1).isEqualTo(asistence2);
        asistence2.setId(2L);
        assertThat(asistence1).isNotEqualTo(asistence2);
        asistence1.setId(null);
        assertThat(asistence1).isNotEqualTo(asistence2);
    }
}
