package eu.monnetproject.translation.sources.iate;

import eu.monnetproject.config.Configurator;
import eu.monnetproject.lang.Language;
import eu.monnetproject.translation.TranslationSource;
import eu.monnetproject.translation.TranslationSourceFactory;
import java.util.Properties;

public class IATESourceFactory implements TranslationSourceFactory {

	@Override
	public TranslationSource getSource(Language srcLang, Language trgLang) {
		final Properties config = Configurator.getConfig("eu.monnetproject.translation.sources.iate");
		Boolean use = Boolean.parseBoolean(config.getProperty("use"));
		if (use) {
			return new IATESourceWithCache(srcLang, trgLang, config);
		} else {
			return null;
		}
	}
}