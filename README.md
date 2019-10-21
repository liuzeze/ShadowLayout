# ShadowLayout
阴影控件  增加子控件圆角效果,  可以调整圆角大小 阴影偏移   以及设置选择器效果(目前只支持selected,pressed  )

![](https://user-gold-cdn.xitu.io/2019/10/18/16dde32e8644100a?w=291&h=518&f=png&s=35208)


## 使用方式
```
  <lz.com.tools.view.ShadowLayout
        android:id="@+id/ShadowLayout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        
        app:sl_clip_cornerr="false"   //内部空间是否要匹配ShadowLayout 的圆角显示,true活将子控件也设置成圆角
        app:sl_backgroundColor="#ff0000"//选中背景色
        app:sl_cornerRadius="29dp" //圆角
        app:sl_nomalBackgroundColor="#0000ff"//未选中背景色
        app:sl_nomalShadowColor="#550000ff" /未选中/阴影颜色
        app:sl_nomalStrokeColor="#0000ff"  //未选中线条颜色
        app:sl_offsetX="0dp"//X轴偏移
        app:sl_offsetY="5dp"//Y轴偏移
        app:sl_leftShow="true"//是否在左边显示阴影
        app:sl_rightShow="true"//是否在右边显示阴影
        app:sl_bottomShow="true"//是否在下边显示阴影
        app:sl_topShow="true"//是否在上边 显示阴影
        app:sl_shadowBlur="10dp"//阴影的扩散范围
        app:sl_shadowColor="#55ff0000"//选中隐隐的颜色
        app:sl_strokeColor="#550000ff">  //选中线条的颜色
```



