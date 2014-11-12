canoe-server
============

a game server framework

该框架是过去游戏服务器开发过程中提炼出来的一些可通用的底层机制，因水平有限，框架自有其包含的主要功能如下：

1. 标注方式的通讯处理，解耦业务逻辑与通讯逻辑：

@CanoeCommand(value = GameProtocol.C_EXIT, roleRequired = true)
	public void onExit(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		session.setRole(null);
	}

2. 事件管理机制，并且包含标注方式的事件侦听，该机制在我们自己项目里的解耦业务逻辑与通讯逻辑起到与上一条同等重要的作用。

	@EventListener(event = SessionEvent.ROLE_CHANGED)
	public void onGameStart(Event<Session> event) {
		Session s = event.getTarget();

    if(s.getRole() == null)
    {
      exit(); //伪代码
  	}
	}

3. 一个简单的线程池模型，可根据需求分组，并且根据开发者提供的一致性代码来实现数据一致性，该线程池同时支持顺序实行和定时执行功能，且可保证定是执行与顺序执行的数据一致性，在提供合理的一致性代码前提下无需加锁。

因时间有限，无法提供完备的文档，对该代码有兴趣者取之，可随意修改以供己用。
