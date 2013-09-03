import akka.actor.{ Actor, ActorRef, ActorSystem, FSM, Props }

class MyFSM extends Actor with FSM[Int, String] {
  startWith(1, "")
  when(1) {
    case Event("go", _) ⇒ goto(2) using "go"
  }
  when(2) {
    case Event("back", _) ⇒ goto(1) using "back"
  }

  onTransition {
    case 1 -> 2 =>
      println("1 -> 2")
    case 2 -> 1 =>
      println("2 -> 1")
  }
}

val system = ActorSystem("MyFSMSystem")
val my_fsm = system.actorOf(Props(new MyFSM()))
// my_fsm ! "go"
my_fsm ! "back"
Thread.sleep(1000)
system.stop(my_fsm)
system.shutdown()

// https://github.com/ngocdaothanh/code-lock-fsm-akka/blob/master/src/main/scala/demo/Demo.scala
// https://github.com/ngocdaothanh/code-lock-fsm-akka/blob/master/src/main/scala/demo/CodeLock.scala