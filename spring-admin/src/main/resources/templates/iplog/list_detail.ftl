<#list pageResult.content as ipLog>
<tr>
	<td>${ipLog.username}</td>
	<td>${ipLog.loginTime?datetime}</td>
	<td>${ipLog.ip}</td>
	<td>${ipLog.displayState}</td>
</tr>
</#list>

<script type="text/javascript">
	$(function(){
		$("#page_container").empty().append($('<ul id="pagination" class="pagination"></ul>'));
		$("#pagination").twbsPagination({
			totalPages:${pageResult.totalPages}||1,
			startPage:${pageResult.currentPage},
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
