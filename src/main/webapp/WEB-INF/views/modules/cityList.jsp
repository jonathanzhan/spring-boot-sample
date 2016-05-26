<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
	<title>Title</title>
</head>
<body>
<table>
	<thead>
		<tr>
			<th>id</th>
			<th>name</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${list}" var="city">
		<tr>
			<td>${city.id}</td>
			<td>${city.name}</td>
		</tr>
	</c:forEach>

	</tbody>
</table>

</body>
</html>
