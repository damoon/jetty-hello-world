import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.core.InjectParam;

@Singleton
@Path("/helloguice")
public class HelloGuice
{
	private final Logger logger;

	private final GuicyInterface gi;

	@Inject
	public HelloGuice(final GuicyInterface gi, final Logger logger)
	{
		this.gi = gi;
		this.logger = logger;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String get(@QueryParam("x") String x, @InjectParam HttpRequestContext httpRequestContext)
	{
		logger.warn("abc");
		return "Howdy Guice. # " + httpRequestContext.getAcceptableLanguages() + " # " + "Injected impl " + gi.toString() + ". Injected query parameter "
				+ (x != null ? "x = " + x : "x is not injected");
	}
}