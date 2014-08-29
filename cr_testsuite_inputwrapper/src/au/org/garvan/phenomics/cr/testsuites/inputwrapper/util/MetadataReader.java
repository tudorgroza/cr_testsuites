package au.org.garvan.phenomics.cr.testsuites.inputwrapper.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import au.org.garvan.phenomics.cr.testsuites.common.api.input.exception.InvalidMetadataException;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.impl.DataSourceMetadata;
import au.org.garvan.phenomics.cr.testsuites.common.util.CommonUtil;
import au.org.garvan.phenomics.cr.testsuites.log.TestSuitesLogger;

public class MetadataReader {

	@SuppressWarnings("unchecked")
	private static TestSuitesLogger<MetadataReader> logger = (TestSuitesLogger<MetadataReader>) TestSuitesLogger
			.getLogger(MetadataReader.class);

	private DataSourceMetadata metadata;
	private String mainFolder;
	private String errorMessage;
	
	public MetadataReader(Properties properties, String mainFolder) {
		metadata = new DataSourceMetadata();
		this.mainFolder = mainFolder;
		this.errorMessage = null;
		
		readMetadata(properties);
	}
	
	private void readMetadata(Properties properties) {
		String dsFile = properties.getProperty(IWProperties.P_DS_FILE);
		dsFile = CommonUtil.checkAndRelocateFolder(mainFolder, dsFile, CommonUtil.PATH_PATTERN);
		metadata.setLocation(dsFile);

		String specFile = properties.getProperty(IWProperties.P_DS_SPEC);
		specFile = CommonUtil.checkAndRelocateFolder(mainFolder, specFile, CommonUtil.PATH_PATTERN);
		metadata.setSpecFile(specFile);
		
		if (dsFile == null) {
			errorMessage = "Unable to process data source file: " + dsFile;
		}
		if (specFile == null) {
			errorMessage = "Unable to process data source spec file: " + specFile;
		}
		
		if (properties.containsKey(IWProperties.P_DS_METADATA)) {
			String metadataFile = properties.getProperty(IWProperties.P_DS_METADATA);
			metadataFile = CommonUtil.checkAndRelocateFolder(mainFolder, metadataFile, CommonUtil.PATH_PATTERN);
			readMetadataFromJSON(metadataFile);
		} else {
			if (properties.containsKey(IWProperties.P_DS_TITLE)) {
				metadata.setTitle(properties.getProperty(IWProperties.P_DS_TITLE));
			}
			if (properties.containsKey(IWProperties.P_DS_ACRONYM)) {
				metadata.setAcronym(properties.getProperty(IWProperties.P_DS_ACRONYM));
			}
			if (properties.containsKey(IWProperties.P_DS_DESCRIPTION)) {
				metadata.setDescription(properties.getProperty(IWProperties.P_DS_DESCRIPTION));
			}
			if (properties.containsKey(IWProperties.P_DS_HOMEPAGE)) {
				metadata.setUrl(properties.getProperty(IWProperties.P_DS_HOMEPAGE));
			}
			if (properties.containsKey(IWProperties.P_DS_RELEASEDATE)) {
				metadata.setReleaseDate(properties.getProperty(IWProperties.P_DS_RELEASEDATE));
			}
		}
	}

	private void readMetadataFromJSON(String metadataFile) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(metadataFile));
			if (jsonObj.containsKey(IWProperties.JSON_DS_METADATA_RELEASEDATE)) {
				//"2014-04-25T08:33:19-07:00
				String releaseDate = (String) jsonObj.get(IWProperties.JSON_DS_METADATA_RELEASEDATE);
				metadata.setReleaseDate(releaseDate);
			}
			if (jsonObj.containsKey(IWProperties.JSON_DS_METADATA_DESCRIPTION)) {
				metadata.setDescription((String) jsonObj.get(IWProperties.JSON_DS_METADATA_DESCRIPTION));
			}
			if (jsonObj.containsKey(IWProperties.JSON_DS_METADATA_HOMEPAGE)) {
				metadata.setUrl((String) jsonObj.get(IWProperties.JSON_DS_METADATA_HOMEPAGE));
			}
			if (jsonObj.containsKey(IWProperties.JSON_DS_METADATA_ACRONYM)) {
				metadata.setAcronym((String) jsonObj.get(IWProperties.JSON_DS_METADATA_ACRONYM));
			}
			if (jsonObj.containsKey(IWProperties.JSON_DS_METADATA_TITLE)) {
				metadata.setTitle((String) jsonObj.get(IWProperties.JSON_DS_METADATA_TITLE));
			}
		} catch (ParseException | IOException e) {
			e.printStackTrace();
			errorMessage = "Unable to read JSON metadata file: " + metadataFile;
			logger.error(e.getMessage());
		}
	}

	public DataSourceMetadata getMetadata() throws InvalidMetadataException {
		if (errorMessage != null) {
			throw new InvalidMetadataException(errorMessage);
		}
		
		return metadata;
	}
}
