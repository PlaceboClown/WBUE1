<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/jsp" xml:lang="de" lang="de">
<jsp:useBean id="game" scope="session"
	class="at.ac.tuwien.big.we15.lab2.api.impl.SimpleGame" />
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Business Informatics Group Jeopardy! - Gewinnanzeige</title>
<link rel="stylesheet" type="text/css" href="style/base.css" />
<link rel="stylesheet" type="text/css" href="style/screen.css" />
<script src="js/jquery.js" type="text/javascript"></script>
<script src="js/framework.js" type="text/javascript"></script>
</head>
<body id="winner-page">
	<a class="accessibility" href="#winner">Zur Gewinnanzeige springen</a>
	<!-- Header -->
	<header role="banner" aria-labelledby="bannerheading">
	<h1 id="bannerheading">
		<span class="accessibility">Business Informatics Group </span><span
			class="gametitle">Jeopardy!</span>
	</h1>
	</header>

	<!-- Navigation -->
	<nav role="navigation" aria-labelledby="navheading">
	<h2 id="navheading" class="accessibility">Navigation</h2>
	<ul>
		<li><a class="orangelink navigationlink" id="logoutlink"
			title="Klicke hier um dich abzumelden" href="login.jsp" accesskey="l">Abmelden</a></li>
	</ul>
	</nav>

	<% game.setFinished(true); %>

	<!-- Content -->
	<div role="main">
		<section id="gameinfo" aria-labelledby="winnerinfoheading">
		<h2 id="winnerinfoheading" class="accessibility">Gewinnerinformationen</h2>
		<%
			boolean p1_answer = game.getUserPlayer().getLastAnswer();
		%>
		<p
			class="user-info <%out.print(p1_answer ? "positive" : "negative");%>-change">
			<%
				out.print(game.getUserPlayer().getAvatar().getName());
			%>
			(Du) hat
			<%
				out.print(p1_answer ? "richtig" : "falsch");
			%>
			geantwortet:
			<%
				out.print(p1_answer ? "+" : "-");
				out.print(game.getUserPlayer().getLastQuestion() == null
						? "0"
						: game.getUserPlayer().getLastQuestion().getValue());
			%>
			€
		</p>
		<%
			boolean p2_answer = game.getComputerPlayer().getLastAnswer();
		%>
		<p
			class="user-info <%out.print(p2_answer ? "positive" : "negative");%>-change">
			<%
				out.print(game.getComputerPlayer().getAvatar().getName());
			%>
			hat
			<%
				out.print(p2_answer ? "richtig" : "falsch");
			%>
			geantwortet:
			<%
				out.print(p2_answer ? "+" : "-");
				out.print(game.getComputerPlayer().getLastQuestion() == null
						? "0"
						: game.getComputerPlayer().getLastQuestion().getValue());
			%>
			€
		</p>

		<section class="playerinfo leader"
			aria-labelledby="winnerannouncement">
		<h3 id="winnerannouncement">
			Gewinner:
			<%
			game.getFirstPlayer().getAvatar().getName();
		%>
		</h3>
		<img class="avatar"
			src="img/avatar/<%out.print(game.getFirstPlayer().getAvatar().getImageFull());%>"
			alt="Spieler-Avatar <%out.print(game.getFirstPlayer().getAvatar().getName());%>" />
		<table>
			<tr>
				<th class="accessibility">Spielername</th>
				<td class="playername">
					<%
						out.print(game.getFirstPlayer().getAvatar().getName()
								+ (game.getFirstPlayer().isComputed() ? "" : "(Du)"));
					%>
				</td>
			</tr>
			<tr>
				<th class="accessibility">Spielerpunkte</th>
				<td class="playerpoints">
					<%
						out.print(game.getFirstPlayer().getAcc());
					%> €
				</td>
			</tr>
		</table>
		</section> <section class="playerinfo" aria-labelledby="loserheading">
		<h3 id="loserheading" class="accessibility">
			Verlierer:
			<%
			game.getSecoundPlayer().getAvatar().getName();
		%>
		</h3>
		<img class="avatar"
			src="img/avatar/<%out.print(game.getSecoundPlayer().getAvatar().getImageHead());%>"
			alt="Spieler-Avatar <%out.print(game.getSecoundPlayer().getAvatar().getName());%>" />
		<table>
			<tr>
				<th class="accessibility">Spielername</th>
				<td class="playername">
					<%
						out.print(game.getSecoundPlayer().getAvatar().getName()
								+ (game.getSecoundPlayer().isComputed() ? "" : "(Du)"));
					%>
				</td>
			</tr>
			<tr>
				<th class="accessibility">Spielerpunkte</th>
				<td class="playerpoints">
					<%
						out.print(game.getSecoundPlayer().getAcc());
					%> €
				</td>
			</tr>
		</table>
		</section> </section>
		<section id="newgame" aria-labelledby="newgameheading">
		<h2 id="newgameheading" class="accessibility">Neues Spiel</h2>
		<form action="BigJeopardyServlet" method="post">
			<input class="clickable orangelink contentlink" id="new_game"
				type="submit" name="restart" value="Neues Spiel" />
		</form>
		</section>
	</div>
	<!-- footer -->
	<footer role="contentinfo">© 2015 BIG Jeopardy</footer>
	<script type="text/javascript">
		//<![CDATA[
		$(document).ready(function() {
			if (supportsLocalStorage()) {
				localStorage["lastGame"] = new Date().getTime();
			}
		});
		//]]>
	</script>
</body>
</html>