
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceLauncher
{	
	public static void main(String[] args) throws Exception
	{		
		final Injector injector = Guice.createInjector(new JerseyServletModule()
		{
			@Override
			protected void configureServlets()
			{
				// Must configure at least one JAX-RS resource or the
				// server will fail to start.
				bind(HelloGuice.class);
				bind(GuicyInterface.class).to(GuicyInterfaceImpl.class);
				
				// Route all requests through GuiceContainer
				serve("/*").with(GuiceContainer.class);
			}
		});
		
		
		final Server server = new Server(8080);
		final ServletContextHandler sch = new ServletContextHandler(server, "/");
		sch.addEventListener(new InjectorProvider(injector));
		sch.addFilter(GuiceFilter.class, "/*", null);
		sch.addServlet(DefaultServlet.class, "/");

		// Start the server
		server.start();
		server.join();
	}
}