import scala.collection.mutable.ArrayBuffer
trait Observer:
	def update(msg: String):Unit


trait Subject:
	var ob = ArrayBuffer[Observer]()
	def attach(observer: Observer) = 
		ob += observer
	def detach(observer: Observer) = 
		ob -= observer
	def notify(msg: String) = 
		for i: Int <- 0 to ob.size-1 do 
			ob(i).update(msg)

class Bot(var name: String):
	def sendMsg(msg: String) = 
		println(f"[${name}]: ${msg}")

class TempBot(var temp: Double,  name: String ) extends Bot(name), Subject:
	override def sendMsg(msg: String) = 
		println(f"[${name}]: ${msg} C")
	def measure() = 
		temp = temp - 1
		sendMsg(temp.toString)
		notify(f"[${name}]: " + temp.toString + " C")
		
class Logger() extends Observer:
	private var log = ArrayBuffer[String]()
	def subscribe(s: Subject) = 
		s.attach(this)
	def unsubscribe(s: Subject) = 
		s.detach(this)
	def printLog() = 
		for i: Int <- 0 to log.size-1 do
			println(f"${log(i)}")
	override def update(msg: String) = 
		log += msg
	
		
// Main method
@main def run() =
  val bot01 = TempBot(20.0, "Bot01")
  val bot02 = TempBot(25.0, "Bot02")
  val log01 = Logger()
  val log02 = Logger()

  log01.subscribe(bot01)
  log02.subscribe(bot01)
  log02.subscribe(bot02)

  for i <- 1 to 2 do
    bot01.measure()
  log02.unsubscribe(bot01)
  for i <- 1 to 3 do
    bot01.measure()
    bot02.measure()
  println()

  println("[Log01]:")
  log01.printLog()
  println("[Log02]:")
  log02.printLog()
