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
    <version>0.0.2</version>
</dependency>
```

## 使用

### `@Mask`

`@Mask` 注解只能使用在字段上。

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mask
```

使用示例：

```java
@Mask(
    type = MaskType.PHONE,
    character = '*',
    left = 3,
    right = 4
)
private String phone;
```

### `@Mask` 属性

| 属性          | 类型         | 默认值 | 说明        |
| ----------- | ---------- | --- | --------- |
| `type`      | `MaskType` | 无   | 指定字段的脱敏类型 |
| `character` | `char`     | `*` | 脱敏字符      |
| `left`      | `int`      | `0` | 左侧保留位数    |
| `right`     | `int`      | `0` | 右侧保留位数    |

## 脱敏类型

当前 `MaskType` 中定义了以下类型：

```java
public enum MaskType {
    EMAIL,
    PHONE,
    ID_CARD
}
```

分别表示：

* `EMAIL`：邮箱
* `PHONE`：手机号
* `ID_CARD`：证件号

## 手机号脱敏

当前 `PhoneMaskStrategy` 已实现手机号脱敏逻辑。

脱敏规则由 `@Mask` 中的以下参数控制：

* `character`：脱敏字符
* `left`：左侧保留位数
* `right`：右侧保留位数

例如：

```java
@Mask(
    type = MaskType.PHONE,
    character = '*',
    left = 3,
    right = 4
)
private String phone;
```

对于：

```text
13812345678
```

脱敏结果为：

```text
138****5678
```

当原字符串为空时，直接返回原字符串。

当：

```text
left + right >= 字符串长度
```

时，不进行脱敏，直接返回原字符串。

## 当前策略

项目通过 `MaskStrategy` 接口定义脱敏策略：

```java
public interface MaskStrategy {

    String mask(String str, char character, int left, int right);

    MaskType type();
}
```

当前项目中包含以下策略：

### `PhoneMaskStrategy`

对应：

```java
MaskType.PHONE
```

当前已经实现手机号脱敏。

### `EmalMaskStrategy`

对应：

```java
MaskType.EMAIL
```

当前 `mask` 方法返回空字符串。

### `IdCardMaskStrategy`

对应：

```java
MaskType.ID_CARD
```

当前 `mask` 方法返回空字符串。

## 脱敏处理流程

项目通过 `MaskAspect` 对 Controller 方法进行 AOP 处理。

切点：

```java
@Around("execution(* *..controller..*(..))")
```

处理流程：

```text
Controller 方法
      ↓
执行 Controller
      ↓
获取返回对象
      ↓
MaskProcessor
      ↓
查找字段上的 @Mask
      ↓
MaskStrategyFactory
      ↓
根据 MaskType 获取 MaskStrategy
      ↓
执行 mask()
      ↓
设置脱敏后的字段值
      ↓
返回对象
```

## `MaskProcessor`

`MaskProcessor` 通过反射获取对象中的字段，并判断字段上是否存在 `@Mask` 注解。

处理逻辑：

1. 判断对象是否为 `null`
2. 获取对象字段
3. 查找字段上的 `@Mask`
4. 根据 `MaskType` 获取对应的 `MaskStrategy`
5. 获取字段原始值
6. 执行脱敏策略
7. 将脱敏后的结果重新设置到字段

如果字段没有 `@Mask` 注解，则跳过。

如果字段值为 `null`，则跳过。

如果没有找到对应的脱敏策略，则跳过。

## `MaskStrategyFactory`

`MaskStrategyFactory` 用于根据 `MaskType` 获取对应的脱敏策略。

构造时接收所有 `MaskStrategy`：

```java
public MaskStrategyFactory(List<MaskStrategy> strategies)
```

然后根据策略的：

```java
MaskType type()
```

建立对应关系。

如果存在重复的脱敏策略类型，则抛出：

```text
重复的脱敏策略类型：{type}
```

获取策略：

```java
MaskStrategy strategy = maskStrategyFactory.getStrategy(maskType);
```

## 自动配置

项目通过：

```text
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

注册：

```text
io.github.octopus123.datamasking.configuration.DataMaskingAutoConfiguration
```

`DataMaskingAutoConfiguration` 使用：

```java
@AutoConfiguration
@Import(MaskStrategyConfiguration.class)
```

进行自动配置。

自动注册以下 Bean：

* `MaskStrategyFactory`
* `MaskProcessor`
* `MaskAspect`

## 策略配置

`MaskStrategyConfiguration` 将当前项目中的脱敏策略注册到 Spring 容器：

* `PhoneMaskStrategy`
* `EmalMaskStrategy`
* `IdCardMaskStrategy`

策略 Bean 使用：

```java
@ConditionalOnMissingBean(PhoneMaskStrategy.class)
```

进行条件注册。

## 项目结构

```text
data-masking-spring-boot-starter
├── pom.xml
├── README.md
└── src
    └── main
        ├── java
        │   └── io.github.octopus123.datamasking
        │       ├── annotation
        │       │   └── Mask
        │       ├── aspect
        │       │   └── MaskAspect
        │       ├── configuration
        │       │   ├── DataMaskingAutoConfiguration
        │       │   └── MaskStrategyConfiguration
        │       ├── enums
        │       │   └── MaskType
        │       ├── factory
        │       │   └── MaskStrategyFactory
        │       ├── processor
        │       │   └── MaskProcessor
        │       └── stragety
        │           ├── EmalMaskStrategy
        │           ├── IdCardMaskStrategy
        │           ├── MaskStrategy
        │           └── PhoneMaskStrategy
        └── resources
            └── META-INF
                └── spring
                    └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

## Maven 配置

项目使用：

```text
GroupId: io.github.oct-opus123
ArtifactId: data-masking-spring-boot-starter
Version: 0.0.1-SNAPSHOT
```

项目依赖：

* `spring-boot-autoconfigure`
* `spring-boot-configuration-processor`
* `spring-boot-starter-aop`

其中 `spring-boot-configuration-processor` 为可选依赖。

## 开发者

**章鱼博士**

GitHub：

https://github.com/oct-opus123

Email：

[zhangyuge2525366@gmail.com](mailto:zhangyuge2525366@gmail.com)

## License

Apache 2.0

## Issue

项目 Issue：

https://github.com/oct-opus123/data-masking-spring-boot-starter/issues
