package org.peertrust.pteditor;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class MyImageRegistry {
	private static ImageRegistry imageregistry=null;
	
	private MyImageRegistry() {
	}
	
	public static ImageRegistry getImageRegistry() {
		if(imageregistry==null) {
			imageregistry=new ImageRegistry();
			imageregistry.put("policy_default",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/policy_default.GIF"));
			imageregistry.put("policy_mandatory",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/policy_mandatory.GIF"));
			imageregistry.put("policy_default_ignored",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/policy_default_ignored.GIF"));
			imageregistry.put("policy_mandatory_ignored",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/policy_mandatory_ignored.GIF"));
			imageregistry.put("root",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/root.GIF"));
			imageregistry.put("class",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/class.GIF"));
			imageregistry.put("credential",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/credential.GIF"));
			imageregistry.put("file_root",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/file_root.GIF"));
			imageregistry.put("add_policy",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/add_policy.GIF"));
			imageregistry.put("remove_policy",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/remove_policy.GIF"));
			imageregistry.put("edit_policy",AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.peertrust.pteditor","icons/edit_policy.GIF"));
		}
		return imageregistry;
	}
}
