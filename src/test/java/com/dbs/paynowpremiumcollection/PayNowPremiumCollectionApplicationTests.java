package com.dbs.paynowpremiumcollection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@EnableJpaRepositories
@ComponentScan(basePackages = {"com.dbs.*"})
class PayNowPremiumCollectionApplicationTests {

	@Test
	void contextLoads() {
	}
}
