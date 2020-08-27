package mk.com.interworks.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
	@Value("${service.instance.name:product-service}")
	private String instanceName;
	
	@GetMapping(value = "/product")
	public String message() {
		String response = "Response From: " + instanceName;
		return response;
	}
}