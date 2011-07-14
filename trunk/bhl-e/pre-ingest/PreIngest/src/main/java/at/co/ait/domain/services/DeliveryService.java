package at.co.ait.domain.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ReleaseStrategy;

import at.co.ait.domain.oais.DigitalObject;
import at.co.ait.domain.oais.InformationPackageObject;
import at.co.ait.domain.oais.LogGenericObject;

@MessageEndpoint
public class DeliveryService {
	
	private @Autowired LogGenericObject loggenericobject;

	private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

	public InformationPackageObject prepareDelivery(List<DigitalObject> digitalobjects) {
		InformationPackageObject pkg = new InformationPackageObject(loggenericobject);
		pkg.setDigitalobjects(digitalobjects);
		return pkg;
	}

}