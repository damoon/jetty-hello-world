import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class InjectorProvider extends GuiceServletContextListener
{
	private final Injector injector;
	
	public InjectorProvider(final Injector injector)
	{
		this.injector = injector;
	}

	@Override
	protected Injector getInjector()
	{
		return injector;
	}
}