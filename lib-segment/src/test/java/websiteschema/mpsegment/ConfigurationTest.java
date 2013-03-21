package websiteschema.mpsegment;

import junit.framework.Assert;
import org.junit.Test;
import websiteschema.mpsegment.conf.MPSegmentConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationTest {

    @Test
    public void should_load_config_options_from_input_map() {
        Map<String, String> config = new HashMap<String, String>();
        config.put("load.domaindictionary", "true");
        MPSegmentConfiguration conf = new MPSegmentConfiguration(config);
        Assert.assertTrue(conf.isLoadDomainDictionary());

        config.put("load.domaindictionary", "false");
        conf = new MPSegmentConfiguration(config);
        Assert.assertFalse(conf.isLoadDomainDictionary());
    }

}
