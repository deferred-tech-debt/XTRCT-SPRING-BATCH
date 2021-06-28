# Incentives Extract

Incentives console application to extract incentives data from EID_AOS Database to flat files for use by external customers.

## Configuration Note
Incentive extract has been upgraded with Spring Batch library to use chunk oriented programming (i.e. reader, processor, writer).
The application read the records using pagination from EID_AOS database and multiple threads are initiated to extract those records to CSV files.
The following parameters have been made configurable and same can be used to tweak the performance of application. 

`records.per.read` - _Page size i.e. Number of incentive records to be read in one page._

`records.per.writer` _Maximum records to write per thread. Once these number of records are processed (i.e. unmarshalled & ready to write), then a thread will be initialized to write them in CSV files._

`thread.pool.size` _Number of threads created by application to extract the records in CSV files._ 

## Build Notes
The incentives-extract is a Spring Boot module and the jar will be : `'incentives-extract-3.0.0.jar'`
The dist.xml build process will rename the jar as` 'incentives-extract.jar'.
The Extract jar can be run using the command:

`java -ms32m -mx4g -Xloggc:c:\ChromeIncentives\CIS-Extract\logs\US_EN_2.2-gc_log.txt -Dlog4j.configuration=file:C:\ChromeIncentives\CIS-Extract\log4j.properties -Dproperties.path=C:\ChromeIncentives\CIS-Extract\jdbc.properties -cp C:\ChromeIncentives\CIS-Extract\lib\* -jar C:\ChromeIncentives\CIS-Extract\lib\incentives-extract.jar -responseDir C:\ChromeIncentives\CIS-Extract\Data\ -locale US_EN -timeout 1000 1>.\Data\extractlog\US_EN_2.2.log
`
The Post-Processor jar can be run using the command:
`java -ms32m -mx4g -Dfile.encoding=UTF-8 -jar C:\ChromeIncentives\CIS-Extract\lib\incentives-extract-post-processor.jar -extractPath C:\ChromeIncentives\CIS-Extract\Data\Data\ConsumerExtract_3.1_en_US.zip -outPath C:\ChromeIncentives\CIS-Extract\Data\ -versions 2.2,2.1,2.0
`
## Performance Tuning
Because of continues increasing incentives record in EID_AOS database, it may be possible (in future) extract takes too much long time to finish. In this case above configuration can be changed to tweak the performance of extract.

##### For Example
1 - If `records.per.read` is increased by 1.5x times, it is necessary to also increase the runtime memory i.e. mentioned under VM args as `Xmx`. Only increasing the `Xmx` may not improve the performance until application is throwing the OutOfMemory Exception.

2 - Also please make sure that `thread.pool.size` should not be less than 1.5 * (`records.per.read` **/** `records.per.writer`), so the records ready to write may not filling up the memory  and being extracted as soon as they are processed.

## Continuous Integration

http://pdxjenkins.autodatacorp.org/jenkins/view/Incentives/job/incentives-extract/

## QA / Prod Build Server

http://teamcity.london.autodata.net/viewType.html?buildTypeId=AutodataOfferSystemAos_Incentives_20_IncentivesExtract


## Current development activity and branch information

https://git.autodatacorp.org/ChromeData/Incentives/extract/wikis/home