---
marp: false
---
# Scala 3, Here I Come!
## 張瑋修 Walter Chang
### @weihsiu / weihsiu@gmail.com
![https://github.com/weihsiu/hereicome](qrcode.png)

---
# Agenda
- What is Scala 3?
- New Syntax
- Select.scala
- Select2.scala
- Actor.scala
- Door.scala
- It.scala
- Cupcakes.scala
- Kvs1.scala
- Kvs2.scala
- NetIO.scala
- Serde.scala
- Kvs3.scala
- Q&A
---
# What is Scala 3?
- Next generation 
- Coming out Fall 2020
- Many new features
  - Source code is mostly backward compatible with Scala 2
- VSCode with Dotty Language Server plugin
---
# New Syntax
- Optional
- **New Control Syntax**
- **Significant Indentation**
- Compiler switches allow to go back and forth
---
# Select.scala
- **Anonymous Given Instances**
- **Extension Methods**
- **Main Functions**
- **Given Imports**
---
# Select2.scala
- **Opaque Type Aliases**
- **Given Parameters**
---
# Actor.scala
- A minimum actor implementation taken shamelessly from @li_haoyi
- **Union Types**
---
# Door.scala
- Taken from an Idris example
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
- New take on the infamous Cake Pattern in Scala 2
- **Implicit Function Types**
  - As return value
- **summon** aka. "the"
- **Intersection Types**
- **Export Clauses**
---
# Kvs1.scala
- Typeclass Oriented Programming (TOP)
- A better way to organize program
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
- Typed key value store
- **Named Type Arguments**
---
# Q&A
![https://github.com/weihsiu/hereicome](qrcode.png)
