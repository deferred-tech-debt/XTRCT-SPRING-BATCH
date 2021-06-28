package com.chromedata.incentives.extract.presentation;

import com.chromedata.commons.dsvio.DSVIO;
import com.chromedata.commons.dsvio.DSVIOBuilder;
import com.chromedata.commons.dsvio.object.util.Delimiter;
import com.chromedata.commons.dsvio.object.util.Qualifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * This class writes the data for given csv files
 */
public class CSVDataSetWriter<T> {

    private static final Logger LOG = LogManager.getLogger(CSVDataSetWriter.class);

    private final Class<T> objectType;
    private DSVIO<T> dsvWriter;

    public CSVDataSetWriter(final Class<T> objectType) {
        this.objectType = objectType;
    }

    public void openFile(File parentDir) throws IOException {
        synchronized (this.objectType) {
            if(this.dsvWriter == null) {
                this.dsvWriter = createDSVWriter(new FileWriter(new File(parentDir, this.objectType.getSimpleName() + ".csv"), true));
            }
        }
    }

    private DSVIO<T> createDSVWriter(final FileWriter fileWriter) throws IOException {
        return new DSVIOBuilder<>(fileWriter, this.objectType)
            .withDelimiter(Delimiter.COMMA)
            .withQualifier(Qualifier.QUOTE)
            .hasQualifiedHeaders(false)
            .withEndOfLine("\r\n")
            .build();
    }

    /**
     * This method takes a collection of DSVAnnotated objects and writes them into a file titled as the class' name
     *
     * @param objects data that will be the rows of the file
     */
    public void writeCollection(final Collection<T> objects) {
        synchronized (this.objectType) {
            if (! CollectionUtils.isEmpty(objects)) {
                this.dsvWriter.writeObjects(objects,
                                            (object, exception) -> LOG.error("Failed to write {} object to CSV: {}",
                                                                             this.objectType.getSimpleName(),
                                                                             object.toString(),
                                                                             exception));
            }
        }
    }

    public void closeFile() throws IOException {
        this.dsvWriter.close();
    }
}