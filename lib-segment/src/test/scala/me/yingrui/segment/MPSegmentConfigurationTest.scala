package me.yingrui.segment

import junit.framework.Assert._
import org.junit.Test
import me.yingrui.segment.conf.MPSegmentConfiguration

class MPSegmentConfigurationTest {

  @Test
  def should_load_configuration_file_via_classpath() {
    assertTrue(MPSegmentConfiguration().is("test.field"))
  }

  @Test
  def should_load_config_options_from_input_map() {
    val loadDomainDictionary = MPSegmentConfiguration().isLoadDomainDictionary()
    val config = Map[String, String]("load.domaindictionary" -> (!loadDomainDictionary).toString)

    val configuration = MPSegmentConfiguration(config)
    assertTrue(configuration.isLoadDomainDictionary() != loadDomainDictionary)
  }

}
