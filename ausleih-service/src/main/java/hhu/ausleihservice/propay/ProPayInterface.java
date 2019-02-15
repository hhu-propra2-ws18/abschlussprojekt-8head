package hhu.ausleihservice.propay;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
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
			System.out.println("getAccountInfo failed");
		}
		return new ProPayAccount();
	}

	public ProPayAccount addFunds(String proPayName, double amount) {
		try {
			final Mono<ProPayAccount> mono
					= WebClient
					.create()
					.post()
					.uri(builder -> builder.scheme("http")
							.host("localhost")
							.port(8888)
							.pathSegment("account", proPayName)
							.queryParam("amount", amount)
							.build())
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(ProPayAccount.class);
			return mono.block();
		} catch (Exception e) {
			System.out.println("addFunds failed");
		}
		return new ProPayAccount();
	}

	public void transferFunds(String sourceAccount, String targetAccount, double amount) {
		try {
			String response = WebClient
					.create()
					.post()
					.uri(builder -> builder.scheme("http")
							.host("localhost")
							.port(8888)
							.pathSegment("account", sourceAccount, "transfer", targetAccount)
							.queryParam("amount", amount)
							.build())
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (Exception e) {
			System.out.println("trnsferFunds failed");
		}
	}

	public ProPayReservation createReservation(String account, String targetAccount, double amount) {
		try {
			final Mono<ProPayReservation> mono
					= WebClient
					.create()
					.post()
					.uri(builder -> builder.scheme("http")
							.host("localhost")
							.port(8888)
							.pathSegment("reservation", "reserve", account, targetAccount)
							.queryParam("amount", amount)
							.build())
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(ProPayReservation.class);
			return mono.block();
		} catch (Exception e) {
			System.out.println("createReservation failed");
		}
		return new ProPayReservation();
	}

	public ProPayAccount releaseReservation(long reservationId, String account) {
		try {
			final Mono<ProPayAccount> mono
					= WebClient
					.create()
					.post()
					.uri(builder -> builder.scheme("http")
							.host("localhost")
							.port(8888)
							.pathSegment("reservation", "release", account)
							.queryParam("reservationId", reservationId)
							.build())
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(ProPayAccount.class);
			return mono.block();
		} catch (Exception e) {
			System.out.println("releaseReservation failed");
		}
		return new ProPayAccount();
	}

	public ProPayAccount punishReservation(long reservationId, String account) {
		try {
			final Mono<ProPayAccount> mono
					= WebClient
					.create()
					.post()
					.uri(builder -> builder.scheme("http")
							.host("localhost")
							.port(8888)
							.pathSegment("reservation", "punish", account)
							.queryParam("reservationId", reservationId)
							.build())
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.retrieve()
					.bodyToMono(ProPayAccount.class);
			return mono.block();
		} catch (Exception e) {
			System.out.println("punishReservation failed");
		}
		return new ProPayAccount();
	}
}
