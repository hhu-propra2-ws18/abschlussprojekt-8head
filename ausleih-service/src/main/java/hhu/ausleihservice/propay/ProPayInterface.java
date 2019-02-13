package hhu.ausleihservice.propay;

import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ProPayInterface {

	public ProPayAccount getAccountInfo(String proPayName) {
		try {
			final Mono<ProPayAccount> mono
					= WebClient
					.create()
					.get()
					.uri(builder -> builder.scheme("http")
							.host("localhost")
							.port(8888)
							.pathSegment("account", proPayName)
							.build())
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(ProPayAccount.class);
			return mono.block();
		} catch (Exception e) {
			System.out.println("GET Events Failed");
		}
		return new ProPayAccount();
	}

	public ProPayAccount addFunds(String proPayName, double amount) {

		LinkedMultiValueMap map = new LinkedMultiValueMap();
		map.add("account", proPayName);
		map.add("amount", amount);

		try {
			final Mono<ProPayAccount> mono
					= WebClient
					.create()
					.post()
					.uri(builder -> builder.scheme("http")
							.host("localhost")
							.port(8888)
							.pathSegment("account", proPayName)
							.build())
					.body(BodyInserters.fromMultipartData(map))
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(ProPayAccount.class);
			return mono.block();
		} catch (Exception e) {
			System.out.println("GET Events Failed");
		}
		return new ProPayAccount();
	}

	public void transferFunds(String sourceAccount, String targetAccount, double amount) {
		LinkedMultiValueMap map = new LinkedMultiValueMap();
		map.add("sourceAccount", sourceAccount);
		map.add("targetAccount", targetAccount);
		map.add("amount", amount);

		try {
			String response = WebClient
					.create()
					.post()
					.uri(builder -> builder.scheme("httpp")
							.host("localhost")
							.port(8888)
							.pathSegment("account", sourceAccount, "transfer", targetAccount)
							.build())
					.body(BodyInserters.fromMultipartData(map))
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ProPayReservation createReservation(String sourceAccount, String targetAccount, double amount) {

		LinkedMultiValueMap map = new LinkedMultiValueMap();
		map.add("sourceAccount", sourceAccount);
		map.add("targetAccount", targetAccount);
		map.add("amount", amount);

		try {
			final Mono<ProPayReservation> mono
					= WebClient
					.create()
					.post()
					.uri(builder -> builder.scheme("http")
							.host("localhost")
							.port(8888)
							.pathSegment("reservation", "reserve", sourceAccount, targetAccount)
							.build())
					.body(BodyInserters.fromMultipartData(map))
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(ProPayReservation.class);
			return mono.block();
		} catch (Exception e) {
			System.out.println("GET Events Failed");
		}
		return new ProPayReservation();
	}

	public ProPayAccount releaseReservation(long reservationId, String account) {
		LinkedMultiValueMap map = new LinkedMultiValueMap();
		map.add("reservationId", reservationId);
		map.add("account", account);

		try {
			final Mono<ProPayAccount> mono
					= WebClient
					.create()
					.post()
					.uri(builder -> builder.scheme("http")
							.host("localhost")
							.port(8888)
							.pathSegment("reservation", "release", account)
							.build())
					.body(BodyInserters.fromMultipartData(map))
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(ProPayAccount.class);
			return mono.block();
		} catch (Exception e) {
			System.out.println("GET Events Failed");
		}
		return new ProPayAccount();
	}

	public ProPayAccount punishReservation(long reservationId, String account) {
		LinkedMultiValueMap map = new LinkedMultiValueMap();
		map.add("reservationId", reservationId);
		map.add("account", account);

		try {
			final Mono<ProPayAccount> mono
					= WebClient
					.create()
					.post()
					.uri(builder -> builder.scheme("http")
							.host("localhost")
							.port(8888)
							.pathSegment("reservation", "punish", account)
							.build())
					.body(BodyInserters.fromMultipartData(map))
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(ProPayAccount.class);
			return mono.block();
		} catch (Exception e) {
			System.out.println("GET Events Failed");
		}
		return new ProPayAccount();
	}
}
