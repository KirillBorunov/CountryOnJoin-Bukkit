package sososlik.countryonjoin;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.CountryResponse;

public class GeoIP implements AutoCloseable {

	private DatabaseReader geoipdbreader;
	
	public GeoIP(File geoipdbFile) throws IOException {
		this.geoipdbreader = new DatabaseReader.Builder(geoipdbFile).withCache(new CHMCache()).build();
	}

	public String findCountryCode(InetAddress address) throws NotFoundException, Exception {
		
		CountryResponse res;
		try {
			res = this.geoipdbreader.country(address);
		}
		catch (AddressNotFoundException e) {
			throw new NotFoundException();
		}

		if(res == null) throw new NotFoundException();

		return res.getCountry().getIsoCode();
	}

	@Override
	public void close() throws Exception {
		this.geoipdbreader.close();
	}
	
	@SuppressWarnings("serial")
	public class NotFoundException extends Exception {

	    public NotFoundException(String message) {
	        super(message);
	    }

		public NotFoundException() {
		}

	}
	
}
