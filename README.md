# data-masking-spring-boot-starter

通过注解的方式对用户数据进行数据脱敏。

## 项目介绍

`data-masking-spring-boot-starter` 是一个 Spring Boot Starter，通过 `@Mask` 注解对字段进行数据脱敏。

项目通过 Spring Boot 自动配置和 AOP，在 Controller 方法执行完成后，对返回对象中标记了 `@Mask` 注解的字段进行脱敏处理。

## 环境

* Java 17
* Spring Boot 3.0.2

## Maven

项目当前坐标：

```xml
<dependency>
    <groupId>io.github.oct-opus123</groupId>
    <artifactId>data-masking-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用

在需要进行数据脱敏的字段上添加 `@Mask` 注解即可。

例如：

```java
public class User {

    @Mask(type = MaskType.PHONE)
    private String phone;

    @Mask(type = MaskType.EMAIL)
    private String email;

    @Mask(type = MaskType.ID_CARD)
    private String idCard;
}
```

也可以通过 `@Mask` 指定脱敏字符、左侧保留位数和右侧保留位数：

```java
@Mask(
    type = MaskType.PHONE,
    character = '*',
    left = 3,
    right = 4
)
private String phone;
```

`@Mask` 只能使用在字段上。

### @Mask 属性

| 属性 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `type` | `MaskType` | 无 | 指定脱敏类型 |
| `character` | `char` | `*` | 脱敏字符 |
| `left` | `int` | `0` | 左侧保留位数 |
| `right` | `int` | `0` | 右侧保留位数 |

## 支持的脱敏类型

当前支持以下脱敏类型：

| 类型 | 说明 |
| --- | --- |
| `PHONE` | 手机号脱敏 |
| `EMAIL` | 邮箱脱敏 |
| `ID_CARD` | 身份证号脱敏 |

### PHONE

手机号默认保留左侧 3 位和右侧 4 位。

```text
15397922663
```

脱敏后：

```text
153****2663
```

也可以通过 `left` 和 `right` 自定义保留位数：

```java
@Mask(
    type = MaskType.PHONE,
    left = 2,
    right = 3
)
private String phone;
```

### EMAIL

邮箱脱敏以 `@` 为分界，只对 `@` 左侧的邮箱用户名进行脱敏，`@` 右侧的域名不进行处理。

例如：

```text
zhangsan@gmail.com
```

### ID_CARD

身份证号可以通过 `@Mask` 指定 `ID_CARD` 类型进行脱敏：

```java
@Mask(type = MaskType.ID_CARD)
private String idCard;
```

## License

Apache 2.0