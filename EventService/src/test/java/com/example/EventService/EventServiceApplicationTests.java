package com.example.EventService;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventServiceApplicationTests {

	@Test
	void applicationClassExists() {
		// Verify the main application class exists
		assertThat(EventServiceApplication.class).isNotNull();
	}

	@Test
	void mainMethodExists() throws NoSuchMethodException {
		// Verify the main method exists
		assertThat(EventServiceApplication.class.getMethod("main", String[].class)).isNotNull();
	}
}
