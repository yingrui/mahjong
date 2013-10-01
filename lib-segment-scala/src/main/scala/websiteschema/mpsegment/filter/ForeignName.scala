package websiteschema.mpsegment.filter

import collection.mutable

class ForeignName {

  def loadNameWord() {
    for (i <- 0 until foreignNameChars.length) {
      foreignNameCharSet += (foreignNameChars.charAt(i))
    }
    for (i <- 0 until foreignNameStartChars.length) {
      foreignNameStartCharSet += (foreignNameStartChars.charAt(i))
    }
  }

  private def containsChar(ch: Char) = foreignNameCharSet.contains(ch)

  def isForeignName(name: String) = name.forall(ch => containsChar(ch))

  def isForeignNameStartChar(start: String) = foreignNameStartCharSet.contains(start.charAt(0))

  private val foreignNameChars = "阿啊哀埃艾爱安昂奥巴拔跋白柏拜班邦包保堡葆鲍北贝倍本比彼俾毕庇弼壁边别宾滨波玻伯孛泊勃博卜布采蔡灿仓藏曹策岑查察柴昌彻陈楚垂春茨慈次聪崔存措达大代戴黛丹当道得德登邓狄狄迪笛底地帝第蒂迭丁东都窦杜端敦顿多厄恩尔耳法凡范方菲腓斐费芬丰冯逢佛夫弗伏伏福甫辅傅富伽盖甘冈高戈哥歌格葛各根艮更贡古顾瓜关光圭郭果哈海罕汉翰杭豪贺贺赫黑亨洪侯胡华怀环黄惠活霍基吉计季济加佳迦嘉贾坚简江姜焦杰捷金津京鸠久居聚均喀卡开凯恺坎康考柯科可克肯孔扣寇库夸宽匡奎魁坤昆阔拉腊来莱赉赖兰朗劳乐勒雷蕾莉黎礼李里理历立丽利荔连莲廉良廖列烈林琳霖麟灵龄刘流留柳龙隆露卢虏鲁禄路吕律略仑伦罗萝洛马玛迈麦满曼芒毛茅梅美门蒙孟米密缅苗闵敏名明缪谟摩沫莫墨默姆木慕穆拿内那纳娜乃奈南瑙讷嫩能妮尼年涅聂宁牛纽农努女诺欧帕派潘庞培佩朋彭蓬丕皮匹片平泼珀破蒲朴普漆齐奇琪契恰钱强乔切钦琴沁青琼丘邱秋求曲屈然冉让饶热仁任日荣容茹儒瑞若撒萨塞赛三桑瑟森沙莎山珊缮尚绍舍申生圣盛诗施什石史士寿舒顺朔丝司思斯松叟苏素绥孙梭所索塔台泰坦汤唐陶特滕藤梯提悌惕田铁汀廷亭通透图徒徒屠土吐托脱陀娃瓦万旺威薇为韦维伟卫魏温文翁沃渥乌伍武西希悉锡熙席霞夏先显相香向萧晓肖歇谢辛欣兴姓幸雄休修秀胥许叙雪逊雅亚娅延扬阳尧耀耶叶伊依义易意因英永尤于雨元约云宰赞早藻泽曾扎詹占湛张章哲者珍真芝知治智忠朱准卓兹子宗祖尊左佐阿博特"
  private val foreignNameStartChars = "丝丹丽乔亚亨伊伦佐保克兰凯切列利加劳南卡卢卫史吉哈唐埃基堂塔塞夏大奥妮姬威娜安富尤尼布希帕康弗彼德戴所托拉文斯旺昆普曼本朱李杰林查格桃桑梅比汉汤沃沙波泰洛海温爱特玛珀珍理琳琼瑞科笆简米索约维罗美肖肯艾芬苏苔英范茉莉莎莫莰莱菲萨蒂薇西詹贝贾赖赫路辛达迈道邦采里阿雅雨雪雷霍露马鲍麦默黛"
  private val foreignNameCharSet = mutable.HashSet[Char]()
  private val foreignNameStartCharSet = mutable.HashSet[Char]()
}

object ForeignName {
  val instance = new ForeignName()
  instance.loadNameWord()
  def apply() = instance
}
