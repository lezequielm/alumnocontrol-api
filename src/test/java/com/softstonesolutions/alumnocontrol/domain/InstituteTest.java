package com.softstonesolutions.alumnocontrol.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.softstonesolutions.alumnocontrol.web.rest.TestUtil;

public class InstituteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Institute.class);
        Institute institute1 = new Institute();
        institute1.setId(1L);
        Institute institute2 = new Institute();
        institute2.setId(institute1.getId());
        assertThat(institute1).isEqualTo(institute2);
        institute2.setId(2L);
        assertThat(institute1).isNotEqualTo(institute2);
        institute1.setId(null);
        assertThat(institute1).isNotEqualTo(institute2);
    }
}
