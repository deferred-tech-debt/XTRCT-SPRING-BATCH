package com.chromedata.incentives.extract;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import java.io.File;
import java.util.Locale;

/**
 * JCommander annotated class used for binding command line arguments into a transfer object
 */
public class CommandLineArgs {

    @Parameter(names = "-responseDir", description = "The path to the extract output directory.", required = true, converter = FileConverter.class)
    private File responseDirectory;
    @Parameter(names = "-locale", description = "The locale for the data extract.", required = true, converter = LocaleConverter.class)
    private Locale locale;
    @Parameter(names = "-timeout", description = "Minutes before extract timeout occurs!  Default: 30 minutes.")
    private int timeout;
    @Parameter(names = "-help", help = true)
    private boolean help;

    CommandLineArgs() {
        timeout = 30;
    }

    public File getResponseDirectory() {
        return responseDirectory;
    }

    public Locale getLocale() {
        return locale;
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean isHelp() {
        return help;
    }

    public static class LocaleConverter implements IStringConverter<Locale> {

        public Locale convert(String localeStr) {
            if (localeStr.length() != 5) {
                throw new IllegalArgumentException("The 'locale' specified should be five characters, e.g. 'US_en'.");
            }

            String language = localeStr.substring(3);
            String country = localeStr.substring(0,2);

            return new Locale(language, country);
        }
    }
}
