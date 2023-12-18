package com.fabioqmarsiaj.spring_boot_3_virtual_threads_poc;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.Instant;

@SpringBootApplication
public class SpringBoot3VirtualThreadsPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3VirtualThreadsPocApplication.class, args);
    }

	@Bean
    public ApplicationRunner runner() {
        return args -> {
            var startDate = Instant.now();

            //startTraditionalThreads();
			startVirtualThreads();
            var finishDate = Instant.now();
            System.out.printf("Duration Time(Milliseconds): %s%n", Duration.between(startDate, finishDate).toMillis());
        };
    }

    private void startTraditionalThreads() throws InterruptedException {
        for (int i = 0; i < 100_000; i++) {
            int finalI = i;
            Thread t = new Thread(() -> System.out.println(finalI));
            t.start();
            t.join();
        }
    }

	private void startVirtualThreads() throws InterruptedException {
		for(int i = 0; i < 100_000; i++){
			int finalI = i;
			Thread t = Thread.ofVirtual()
					.name(String.format("virtualThread-%s", i))
					.unstarted(() -> System.out.println(finalI));
			t.start();
			t.join();
		}
	}
}
