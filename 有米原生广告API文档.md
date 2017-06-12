# 有米原生广告API文档

#### 版本说明

* v1.0 - 第一版。
* v1.1 - 增加针对Android的适配。
* v1.2 - 参数列表修改；返回值参数样例修改；修改[效果监控上报](#效果监控上报)的文字说明。
* v1.3 - [返回值参数列表](#返回值参数列表)中广告id调整为string类型；slotid调整为string类型；页面内link修改。
* v1.4 - [请求参数列表（GET）](#请求参数列表（GET）)增加SIM卡参数发送； [效果监控上报](#效果监控上报)增加download和install两个参数。
* v1.4.1 - 修改[请求广告](#请求广告)和[效果监控上报](#效果监控上报)的文字说明。
* v1.4.2 - 参数列表修改；添加AppVersion参数。
* v1.4.3 - 修改deeplink跳转的说明。



#### 文档说明

本文档作为贵司与有米平台间进行对接的指引文档。

在接入API之前，请贵司登录有米开发者后台[http://app.youmi.net](http://app.youmi.net/)，在开发者后台创建好App，并设置好所需的广告位。在创建好App后可以获得对应的appid，在后续的所有API流程中，均需要依靠该appid作授权认证。



## 授权认证

在以下所有API请求中，都需要携带用于作为身份验证的Token。在请求过程中，需要在HTTP Header中携带Token信息。目前Token即是应用的appid。

HTTP Header中携带Token信息的示例。

```json
Authorization: Bearer <Token>
```



## 请求广告

该接口用于请求广告数据。每一次请求都会返回0～N个广告，***一次请求中所返回的广告，每个广告只能展示一次，多次展示无效***。即广告展示完毕后不能再使用，需要重新发起请求获取新的广告数据。***返回的广告需要即刻展示，不能缓存起来***。

请求参数重大部分参数均为非必需参数，但每一个参数(包括非必需参数)，都会影响到实际投放过程中的广告筛选。因此***建议贵司尽可能将文档中所列参数填满***，否则实际运行中可能会出现没有广告填充的问题。



#### 请求URL

| 平台      | URL                                      |
| ------- | ---------------------------------------- |
| iOS     | [https://native.umapi.cn/ios/v1/oreq](https://native.umapi.cn/ios/v1/oreq) |
| Android | [https://native.umapi.cn/aos/v1/oreq](https://native.umapi.cn/aos/v1/oreq) |



#### 请求参数列表（GET）

| 字段          | 类型     | 必须   | 描述                                       |
| ----------- | ------ | ---- | ---------------------------------------- |
| reqtime     | string | 是    | 发起请求的Unix时间戳，精确到秒。                       |
| slotid      | string | 是    | 所需的广告位ID。                                |
| adcount     | string | 是    | 所需要的广告数，不填默认为1，实际返回的广告数小于等于adcount。      |
| reqid       | string | 是    | 这次请求的唯一id。                               |
| idfa        | string | 是    | iOS设备的IDFA，明文不加密；iOS必须填写。                |
| brand       | string | 是    | 制造厂商,如“apple”“Samsung”“Huawei“。          |
| model       | string | 是    | 型号, 如”iphoneA1530”。                      |
| mac         | string | 是    | 设备的mac地址，明文不加密。                          |
| imei        | string | 是    | 设备的imei码，明文不加密; Android必须填写。             |
| androidid   | string | 是    | 设备的android id，明文不加密; Android必须填写。        |
| imsi        | string | 是    | 设备的imsi id, 明文不加密; Android必须填写。          |
| ip          | string | 是    | 当前请求的IP地址，必须为手机客户端发起的IP地址                |
| ua          | string | 是    | UserAgent。                               |
| os          | string | 是    | 操作系统（android，ios）。                       |
| osv         | string | 是    | 操作系统描述的系统版本号。                            |
| appversion  | string | 是    | 应用版本号。会根据版本号投放对应的广告。                     |
| conntype    | string | 是    | 网络类型，空=无，0=未知/其他，1=wifi，2=2g，3=3g，4=4g，5=5g。 |
| carrier     | string | 是    | 网络运营商，空=无，0=未知/其他，1=wifi，2=移动，3=联通，4=电信。 |
| pk          | string | 是    | 安卓为App的包名，iOS为App的BundleIdentifier。      |
| countrycode | string | 是    | 用户设置的国家编码，如CN。                           |
| language    | string | 否    | 用户设置的语言，如zh。                             |
| gender      | string | 否    | 性别，M=男性，F=女性。                            |
| age         | string | 否    | 年龄，如24。                                  |
| cont_title  | string | 否    | 内容的标题。                                   |
| cont_kw     | string | 否    | 内容的关键词，多个关键词用逗号分隔。                       |

*注：建议使用标准URL函数来进行参数封装（会保障所有参数都经过URLEncode），以免出现参数混乱的问题。*



#### 返回值参数列表

| 字段   | 类型     | 说明                                |
| ---- | ------ | --------------------------------- |
| c    | int    | 状态码。0表示正常，其他表示错误，常见错误码见[附1](#附1)。 |
| rsd  | string | RequestSeed，本次会话的唯一码。             |
| ad   | Ad[]   | 广告列表，参见[参数Ad](#参数ad)。             |



##### 参数Ad

| 字段        | 类型      | 说明                                       |
| --------- | ------- | ---------------------------------------- |
| id        | string  | 广告的id，string类型。                          |
| slotid    | string  | 匹配的广告位id，string类型。                       |
| name      | string  | 广告的名字。                                   |
| icon      | string  | 广告的ICON图标，1:1的方形图。                       |
| pic       | Pic[]   | 广告图片素材列表，返回的图片素材数量与广告位所提供的图片数量一致，即广告位提供为单图广告位，则pic数组里仅有一个元素。参见[参数Pic](#参数pic) |
| slogan    | string  | 广告标题，一般字数较少。部分广告位没有广告标题只有广告语。            |
| subslogan | string  | 广告语，一般字数较多。                              |
| url       | string  | iOS平台：点击跳转到的落地页；Android平台：APP广告的下载地址或者WAP广告的页面地址。 |
| uri       | string  | iOS为应用的URLScheme，Android为应用的URI。用于直接打开外部App的某一指定落地页（如电商App直接打开商品详情页）。 |
| pt        | int     | 广告的类型（ 0：APP广告；1：WAP广告）。                 |
| track     | Track[] | 广告监测列表，参见[参数Track](#参数track)。            |
| app       | App{}   | App广告的应用信息，参见[参数App](#参数app)。***注意：即便是App类型的广告，该字段也有可能为空值。*** |
| ext       | Ext{}   | 开源lib所使用的额外参数，可以不用理会。                    |



##### 参数Pic

| 字段   | 类型     | 说明         |
| ---- | ------ | ---------- |
| url  | string | 图片的URL地址。  |
| w    | int    | 图片的宽度（像素）。 |
| h    | int    | 图片的高度（像素）。 |



##### 参数Track

| 字段       | 类型       | 说明                                       |
| -------- | -------- | ---------------------------------------- |
| show     | string[] | 该字段为一组URL的List，里面每一条URL都是一条`曝光`的Tracking链接，具体的使用方法请参见[效果监控上报](#效果监控上报)。 |
| click    | string[] | 该字段为一组URL的List，里面每一条URL都是一条`点击`的Tracking链接，具体的使用方法请参见[效果监控上报](#效果监控上报)。 |
| download | string[] | 该字段为一组URL的List，里面每一条URL都是一条`下载完成`的Tracking链接，具体的使用方法请参见[效果监控上报](#效果监控上报)。 |
| install  | string[] | 该字段为一组URL的List，里面每一条URL都是一条`安装完成`的Tracking链接，具体的使用方法请参见[效果监控上报](#效果监控上报)。 |



##### 参数App

| 字段          | 类型       | 说明                                     |
| ----------- | -------- | -------------------------------------- |
| storeid     | string   | 应用在市场上的storeid，对于iOS应用，该字段为应用的iTunesID |
| bid         | string   | 应用的包名，对于iOS应用，该字段为应用的BundleIdentifier  |
| description | string   | 应用在市场上的描述信息。                           |
| size        | string   | 应用的大小，语义化表示方法。如：53M。                   |
| screenshot  | string[] | 应用在市场上提供的截图，该字段为一组URL的List。            |
| score       | float    | 应用的市场评分，评分从0.0~5.0。                    |
| category    | string   | 应用的分类，文字表示法。如：游戏。                      |



##### 参数Ext

| 字段    | 类型   | 说明                            |
| ----- | ---- | ----------------------------- |
| io    | int  | 浏览器打开方式（0：内部浏览器（默认）；1：外部浏览器）。 |
| delay | int  | 关闭按钮延迟显示时间 单位：秒。              |
| sal   | int  | 是否显示《广告》标识，0:不显示；1:显示。        |
| pl    | int  | 是否显示平台标识，0:不显示；1:显示。          |



##### 返回值样例

```json
{
  "c": 0,
  "rsd": "TADS234211435343f",
  "ad": [
    {
      "id": "55",
      "slotid": 1,
      "name": "皇室战争",
      "icon": "https://demo.youmi.net/icon-55.png",
      "pic": [{
        "url": "https://demo.youmi.net/pic-55.png",
        "w": 600,
        "h": 400
      },{
        "url": "https://demo.youmi.net/pic-55-2.png",
        "w": 600,
        "h": 400
      }],
      "slogan": "部落冲突：皇室战争",
      "subslogan": "又将制霸全球?《部落冲突：皇室战争》已登陆Appstore",
      "url": "https://itunes.apple.com/cn/app/id1053012308?mt=8",
      "uri": "huangshizhanzheng://",
      "pt": 0,
      "track": {
        "show": [
          "http://track1.youmi.net/TADS234211435343f",
          "http://track2.youmi.net/TADS234211435343f"
        ],
        "click": [
          "http://track1-click.youmi.net/TADS234211435343f",
          "http://track2-click.youmi.net/TADS234211435343f"
        ],
        "download": [
          "http://track2-down.youmi.net/TADS234211435343f"
        ],
        "install": [
          "http://track2-install.youmi.net/TADS234211435343f"
        ],
      },
      "app": {
        "storeid": "1053012308",
        "bid": "com.huangshizhanzheng",
        "description": "<应用描述>",
        "size": "223M",
        "screenshot": [
          "http://aaa.png",
          "http://ccc.png",
          "http://bbb.png"
        ],
        "score": 4.5,
        "category": "游戏"
      },
      "ext": {
        "io": 0,
        "delay": 1,
        "sal": 0, 
        "pl": 0 
      }
    },
    {
      // ...
    }
  ]
}
```



## 效果监控上报

**注意：效果监控上报十分重要，需要贵司确保App中已经能够很好的支持效果上报上报功能，否则可能会导致结算问题。**

效果监控上报的数据从[返回值参数列表](#返回值参数列表)的`track`字段中提取。该字段中包含`show`，`click`，`download`和`install` 四个参数，分别用于`曝光监控`，`点击监控`，`下载监控`和`安装激活监控`。每个参数都是一个URL的数组，保存一条或多条URL，一般数组中不会超过5条URL。

**参数说明：**

| 参数       | 说明               |
| -------- | ---------------- |
| show     | 曝光监控。            |
| click    | 点击监控。            |
| download | 下载完成监控，只有安卓平台需要。 |
| install  | 安装完成，只有安卓平台需要。   |

track字段的结构大致如下：

```json
{
  "show": [ 
          "http://track1.youmi.net/impression/adhfoqefqwef", 
          "http://track2.youmi.net/show/dadfoewefqererete" 
        ],
  "click": [ 
          "http://track3.youmi.net/click/dfhehfqeeqfef", 
          "http://track4.youmi.net/click/adfaaadkfdfdd" 
        ],
  // 安卓平台才有 download 和 install 监控
  "download": [
          "http://track5.youmi.net/eff/pdkpfpwwsdsrrtf"
        ],
  "install": [
          "http://track6.youmi.net/eff/wadasafsfryrtrt"
  		]
}
```

**效果监控上报流程如下：**

1. 广告曝光时，调用show列表里的url上报曝光监控，链接需要从**客户端**发起。
2. 用户点击时，调用click列表里的url上报点击监控，需要从**客户端**发起请求。
3. 注意：若click列表不为空，则需要等待点击监控全部发送完成后再跳转到落地页。
4. 安卓平台的用户下载完成时，调用download列表里的url上报下载监控，需要从**客户端**发起请求。
5. 安卓平台的用户安装完成时，调用install列表里的url上报安装监控，需要从**客户端**发起请求。




## iOS点击跳转到落地页的逻辑

**注意：为了防止跳出App后点击记录无法发送而出现结算问题，需要在效果监控上报完成后，再发起点击跳转。点击逻辑较为复杂，建议贵司直接使用有米封装好的开源逻辑类库简化开发工作。**

1. 点击链接有url和uri两个。
2. url为普通的http(s)链接，用于打开wap页或是跳转到AppStore。
3. uri为应用的urlscheme，用于打开外部App的指定内部落地页。
4. 如果是iOS应用推广，一般情况app下的storeid字段会记录对应的itunesid，当这个字段有值时，也可以通过这个字段在应用内打开AppStore。



#### 应用内打开AppStore

在应用内直接打开AppStore的详情页，可以有效提高用户体验与转化率，从而提高应用收入。建议在应用中支持打开AppStore的逻辑。

1. 当返回的广告为一个App广告时，app下的storeid字段记录对应的itunesid，注意storeid字段不一定都有值，有值的情况下才能处理。
2. InApp内打开Store的流程需要特殊处理，并且不能使用正常的url来打开，因此这种情况下url字段可能为空，点击记录由[效果监控上报](#效果监控上报)负责处理。
3. deeplink的逻辑不受影响。


#### URI唤醒

URI唤醒可以通过URLScheme直接打开目标应用的某个特定页面，这种推广模式对于电商等品类的广告有显著效果，支持URI唤醒可以显著提高应用收入，贵司可以在应用中提供对URI唤醒的支持。

1. URI唤醒不能使用正常url来打开，因此点击监控由[效果监控上报](#效果监控上报)负责处理。
2. 当返回值中uri字段不为空时，表明该广告可以进行URI唤醒。
3. 首先通过uri尝试唤醒目标应用，唤醒成功直接跳转到目标App上，不需要再调用url字段。
4. 若唤醒失败，则调用url字段打开AppStore，引导用户下载应用。
5. 若唤醒失败，且app下的storeid字段有值，则可以尝试不调用url字段，直接在应用内打开AppStore引导用户下载。



#### 普通链接推广

普通链接推广一般指点击打开一个普通的WAP页面，可以是跳转到外部Safari，也可以在App内打开一个内置浏览器，直接调用url打开对应的落地页即可。



## Android点击跳转到逻辑页的逻辑

**注意：为了防止跳出App后点击记录无法发送而出现结算问题，需要在效果监控上报完成后，再发起点击跳转。点击逻辑较为复杂，建议贵司直接使用有米封装好的开源逻辑类库简化开发工作。**

1. 检查pt参数，确认广告类型。
2. 根据不同的广告类型合理使用url以及uri两个参数进行跳转。



#### APP广告类型的跳转逻辑

1. 如果app还没有安装，则使用url进行下载安装，安装完毕后，检查uri是否存在，存在就打开uri，不存在就直接根据包名打开app，跳转逻辑结束。
2. 如果app已经安装，uri存在，则把uri解析为Intent，然后打开Intent，跳转逻辑结束
3. 如果app已经安装，uri不存在，这根据包名打开app，跳转逻辑结束。



#### WAP广告类型的跳转逻辑
1. url用于打开一个WAP页面，InApp打开或跳转到外部浏览器均可。





## 附1

#### 常见错误码

| c     | 说明              | 建议处理                              |
| ----- | --------------- | --------------------------------- |
| -2007 | 请求广告失败          |                                   |
| -3003 | 缺少参数            | 填写正确的必要参数                         |
| -2222 | 请求广告超时          | 参数中的reqtime请填写正确时间                |
| -1002 | APPID不存在        | 需要填写正确的APPID                      |
| -3312 | 用户今天的广告展示已经达到上限 | 不展示广告或提示用户“今天可观看/点击广告已达到上线“       |
| -3208 | 设备参数非法或者缺失      | iOS需要填写正确的IDFA，Android需要填写正确的IMEI |
| -1012 | Header错误        | 需要填写正确的Header                     |

