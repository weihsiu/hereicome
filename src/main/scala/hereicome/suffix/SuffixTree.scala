package hereicome.suffix

enum Tree[+A] {
  case Node(x: A, children: Vector[Tree[A]])
  case Leaf
}