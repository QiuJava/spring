<#list page.content as data>
<tr>
	<td>${data.username}</a></td>
	<td>${data.loginTime?datetime}</td>
	<td>${data.ip}</td>
	<td>${data.displayState}</td>
</tr>
</#list>

<script type="text/javascript">
	$(function(){
		$("#page_container").empty().append($('<ul id="pagination" class="pagination"></ul>'));
		$("#pagination").twbsPagination({
			totalPages:${page.totalPages}||1,
			startPage:${page.currentPage},
			first : "首页",
			prev : "上一页",
			next : "下一页",
			last : "最后一页",
			initiateStartPageClick:false,
			onPageClick : function(event, page) {
				$("#currentPage").val(page);
				$("#searchForm").submit();
			}
		});
	});
</script>
