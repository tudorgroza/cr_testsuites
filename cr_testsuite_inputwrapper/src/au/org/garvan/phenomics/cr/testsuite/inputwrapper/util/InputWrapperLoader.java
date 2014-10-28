package au.org.garvan.phenomics.cr.testsuite.inputwrapper.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IInputDataWrapper;
import au.org.garvan.phenomics.cr.testsuite.common.util.CommonUtil;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class InputWrapperLoader {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<InputWrapperLoader> logger = (TestSuiteLogger<InputWrapperLoader>) TestSuiteLogger
			.getLogger(InputWrapperLoader.class);

	private Map<String, IInputDataWrapper> dataWrappers;
	private String mainPath;
	
	public InputWrapperLoader(Properties properties, String mainPath) {
		dataWrappers = new HashMap<String, IInputDataWrapper>();
		this.mainPath = mainPath;
		
		if (!this.mainPath.endsWith("/")) {
			this.mainPath += "/";
		}

		parseProperties(properties);
	}

	private void parseProperties(Properties properties) {
		Map<Integer, Map<String, String>> propValues = new HashMap<Integer, Map<String,String>>();
		
		for (Object key : properties.keySet()) {
			String propName = (String) key;
			
			int index = propName.indexOf("[");
			int index2 = propName.indexOf("]");
			
			int idx = Integer.parseInt(propName.substring(index + 1, index2));
			String actualProperty = propName.substring(index2 + 2);
			
			Map<String, String> map = propValues.containsKey(idx) ? propValues.get(idx) : new HashMap<String, String>();
			map.put(actualProperty, properties.getProperty(propName));
			propValues.put(idx, map);
		}
		
		for (int idx : propValues.keySet()) {
			instatiateWrapper(propValues.get(idx));
		}
	}

	@SuppressWarnings("unchecked")
	private void instatiateWrapper(Map<String, String> map) {
		if (!map.containsKey(IWProperties.P_NAME) && !map.containsKey(IWProperties.P_CLASS)) {
			return;
		}
		
		logger.info("Instantiating wrapper: " + map.get(IWProperties.P_NAME) + " ...");
		Properties properties = retrieveProperties(map);

		try {
			Class<IInputDataWrapper> candidateWrapperCls = (Class<IInputDataWrapper>) Class
					.forName(map.get(IWProperties.P_CLASS));
			IInputDataWrapper candidateWrapper = null;
			if (properties.isEmpty()) {
				candidateWrapper = candidateWrapperCls.getConstructor().newInstance();
				candidateWrapper.setMainPath(mainPath);
			} else {
				candidateWrapper = candidateWrapperCls.getConstructor(Properties.class).newInstance(properties);
				candidateWrapper.setMainPath(mainPath);
			}
			dataWrappers.put(map.get(IWProperties.P_NAME), candidateWrapper);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			
			logger.error("Unable to instantiate wrapper: " + map.get(IWProperties.P_NAME) + " ...");
			logger.error(e.getMessage());
		}
		
		logger.info("Wrapper: " + map.get(IWProperties.P_NAME) + " successfully instatiated ...");
	}

	private Properties retrieveProperties(Map<String, String> propertyMap) {
		Properties properties = new Properties();
		
		for (String key : propertyMap.keySet()) {
			String value = CommonUtil.checkAndRelocateFolder(mainPath, propertyMap.get(key), CommonUtil.PATH_PATTERN);

			if (key.startsWith(IWProperties.P_PROPERTY)) {
				int index = key.indexOf("[");
				String tmp = key.substring(index + 1).trim();
				index = tmp.indexOf("]");
				String actualProperty = tmp.substring(0, index).trim();
				properties.put(actualProperty, value);
			}
		}
		
		return properties;
	}

	public Map<String, IInputDataWrapper> getDataWrappers() {
		return dataWrappers;
	}
}
