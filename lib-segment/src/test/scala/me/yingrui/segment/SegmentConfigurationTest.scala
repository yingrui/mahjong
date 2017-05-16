package me.yingrui.segment

import junit.framework.Assert._
import me.yingrui.segment.conf.SegmentConfiguration
import org.junit.Test

class SegmentConfigurationTest {

  @Test
  def should_load_configuration_file_via_classpath() {
    assertTrue(SegmentConfiguration().is("test.field"))
  }

  @Test
  def should_load_config_options_from_input_map() {
    val loadDomainDictionary = SegmentConfiguration().isLoadDomainDictionary()
    val config = Map[String, String]("load.domaindictionary" -> (!loadDomainDictionary).toString)

    val configuration = SegmentConfiguration(config)
    assertTrue(configuration.isLoadDomainDictionary() != loadDomainDictionary)
  }

}
