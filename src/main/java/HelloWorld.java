import java.util.concurrent.TimeUnit;

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
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.annotation.Timed;

@Singleton
@Path("/helloguice")
public class HelloWorld
{
	private final Logger logger;

	private final GuicyInterface gi;

	private final Meter callsMeter = Metrics.newMeter(HelloWorld.class, "Requests", "calls", TimeUnit.SECONDS);
	
	@Inject
	public HelloWorld(final GuicyInterface gi, final Logger logger)
	{
		this.gi = gi;
		this.logger = logger;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Timed()
	public String get(@QueryParam("x") String x, @InjectParam HttpRequestContext httpRequestContext)
	{
		callsMeter.mark();
		logger.debug("abc");
		return "Howdy Guice. # " + httpRequestContext.getAcceptableLanguages() + " # " + "Injected impl " + gi.toString() + ". Injected query parameter "
				+ (x != null ? "x = " + x : "x is not injected");
	}
}