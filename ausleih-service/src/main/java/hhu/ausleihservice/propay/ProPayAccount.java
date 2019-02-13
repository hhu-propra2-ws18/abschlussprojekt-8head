package hhu.ausleihservice.propay;

import lombok.Data;

import java.util.Set;

@Data
public class ProPayAccount {

	private String accountName;
	private double balance;
	private Set<ProPayReservation> reservations;
}
