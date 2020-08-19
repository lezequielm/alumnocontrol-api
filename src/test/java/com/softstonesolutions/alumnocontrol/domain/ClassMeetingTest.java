package com.softstonesolutions.alumnocontrol.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.softstonesolutions.alumnocontrol.web.rest.TestUtil;

public class ClassMeetingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassMeeting.class);
        ClassMeeting classMeeting1 = new ClassMeeting();
        classMeeting1.setId(1L);
        ClassMeeting classMeeting2 = new ClassMeeting();
        classMeeting2.setId(classMeeting1.getId());
        assertThat(classMeeting1).isEqualTo(classMeeting2);
        classMeeting2.setId(2L);
        assertThat(classMeeting1).isNotEqualTo(classMeeting2);
        classMeeting1.setId(null);
        assertThat(classMeeting1).isNotEqualTo(classMeeting2);
    }
}
