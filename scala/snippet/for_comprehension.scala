val numbers = List(1, 2, 3, 4)
println(numbers)
println(for(n <- numbers) yield n*10)
// numbers.map((i: Int) => println(i))
// numbers.map(println _)
// numbers.map((i: Int) => i+1).foreach(println _)
// numbers.filter((i: Int) => i % 2 == 0).foreach(println _)
// numbers.drop(3).foreach(print _)
// numbers.dropWhile(_ % 2 != 0).foreach(print _)
// List(1, 2, 3).zip(List("a", "b", "c")).foreach(println _)
// (List(List(1, 2, 3), List("a", "b", "c")).flatten).foreach(print _)
// List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).partition(_ % 2 == 0).productIterator.toList map ( println _ )
// println(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).foldLeft(0)((m: Int, n: Int) => m + n))
// println(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).foldRight(0)((m: Int, n: Int) => m + n))
// (List(List(1, 2), List(3, 4)).flatMap(x => x.map(_ * 2))).foreach(println _)