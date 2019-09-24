---
marp: false
---
# Scala 3, Here I Come!
## 張瑋修 Walter Chang
### @weihsiu / weihsiu@gmail.com
![https://github.com/weihsiu/hereicome](qrcode.png)

---
# Scala Taiwan
## [Scala Taiwan gitter channel](https://gitter.im/ScalaTaiwan/ScalaTaiwan)
## [Scala Taiwan FB group](https://www.facebook.com/groups/ScalaTW/)
## [Scala Taiwan meetup](https://www.meetup.com/Scala-Taiwan-Meetup/)
![Scala Taiwan Logo](scalataiwan.png)

---
# Agenda
- What is Scala 3?
- New syntax
- Select.scala
- Conversions.scala
- Actor.scala
- Door.scala
- It.scala
- Cupcakes.scala
- Kvs.scala
- Q&A
---
# What is Scala 3?
- Next generation 
- Coming out Fall 2020
- Many new features and cleanups
- Source code is mostly backward compatible with Scala 2
- Libraries from both Scala 2/3 can be used interchangeably
- VSCode with Dotty Language Server plugin
---
# New syntax
- Optional
- **New Control Syntax**
- **Significant Indentation**
- Compiler switches allow to go back and forth
---
# Select.scala
- The ability to select something
- **Anonymous Given Instances**
- **Extension Methods**
- **Main Functions**
- **Given Imports**
---
# Select2.scala
- On top of the ability to select something, abstract how the selection is done.
- **Opaque Type Aliases**
- **Given Parameters**
---
# Conversions.scala
- The new and safer way to define implicit conversion
- **Implicit Conversions**
---
# Actor.scala
- A minimum actor implementation taken shamelessly from @li_haoyi
- **Union Types**
---
# Door.scala
- Translated from an Idris example
- **Enumerations**
- **Dependent Function Types**
- **Toplevel Definitions**
---
# It.scala
- Mimics Kotlin's "it"
- **Implicit Function Types**
  - As parameter
---
# Cupcakes.scala
- A minimum DI framework
- New take on the infamous Cake Pattern in Scala 2 ;)
- **Implicit Function Types**
  - As return value
- **summon** aka. "the"
- **Intersection Types**
- **Export Clauses**
---
# Kvs1.scala
- A simple key-value store
- Typeclass Oriented Programming (TOP)
  - A better way to organize program
  - A solution to [the Expression problem](https://en.wikipedia.org/wiki/Expression_problem)
- **Parameter Untupling**
---
# Kvs2.scala
- Abstract effects
  - Identity effect
  - Network IO effect
- **Alias Givens**
---
# NetIO.scala
- Socket IO effect
- **Creator Applications**
---
# Serde.scala
- Serialization / deserialization
---
# Kvs3.scala
- Typed key-value store
- **Named Type Arguments**
---
# Q&A
## That's all and thank you for your attention
![https://github.com/weihsiu/hereicome](qrcode.png)

