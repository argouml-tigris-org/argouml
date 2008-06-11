package org.argouml.profile.plugin;

import org.apache.log4j.Logger;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.profile.ProfileFacade;
import org.tigris.gef.undo.UndoableAction;

/**
 * General superclass for module User Defined profiles defined as plug-in 
 * 
 * @author maas
 */
@SuppressWarnings("serial")
public abstract class PluginProfileModule extends UndoableAction implements ModuleInterface {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(PluginProfileModule.class);

	private PluginProfile  profile;
		
	/**
	 * 
	 */
	public PluginProfileModule() {
		super("plugin profile");
		registerProfile(this.getClass());
	}

	private void registerProfile(Class cl) {
		try {
			PluginProfileLoader loader = PluginProfileLoaderImpl.getInstance();

			profile = loader.loadProfile(cl);
		} catch (ErrorLoadingPluginProfile e) {
			LOG.error("Error loading profile", e);
		}
	}
	
	/**
	 * @see org.argouml.moduleloader.ModuleInterface#disable()
	 */
	public boolean disable() {
		ProfileFacade.getManager().removeProfile(profile.getProfile());
		return true;
	}

	/**
	 * @see org.argouml.moduleloader.ModuleInterface#enable()
	 */
	public boolean enable() {		
		ProfileFacade.getManager().registerProfile(profile.getProfile());
		return true;
	}

	/**
	 * @see org.argouml.moduleloader.ModuleInterface#getInfo(int)
	 */
	public String getInfo(int type) {
		if (profile == null) {
			LOG.debug("Empty Plugin Profile detected!");
			
			return "Empty Plug-in";
		} else {
	        switch (type) {
	        case DESCRIPTION:
	            return profile.getDescription();
	        case AUTHOR:
	            return profile.getAuthor();
	        case VERSION:
	            return profile.getVersion();
	        case DOWNLOADSITE:
	            return profile.getDownloadSite();
	        default:
	            return null;
	        }    
		}
	}

	/**
	 * @see org.argouml.moduleloader.ModuleInterface#getName()
	 */
	public String getName() {
		String ret = null;
		if (profile == null) {
			LOG.debug("Empty Plugin Profile detected!");
			
			ret = "Empty Plug-in";
		} else {
			ret = profile.getName();			
		}
		return ret;
	}

}
