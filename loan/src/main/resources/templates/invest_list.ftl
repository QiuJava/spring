<#if pageResult.content?size &gt; 0 >
	<#list pageResult.content as borrow>
		<tr>
			<td>${borrow.borrower.username}</td>
			<td>${borrow.title}</td>
			<td class="text-info">${borrow.rate}%</td>
			<td class="text-info">${borrow.borrowAmount}</td>
			<td>${borrow.repaymentMethodDisplay}</td>
			<td>
				<div class="">
					${borrow.persent} %
				</div>
			</td>
			<td><a class="btn btn-danger btn-sm"
				href="/website/borrow_info?id=${borrow.id}">查看</a></td>
		</tr>
	</#list>
<#else>
	<tr>
		<td colspan="7" align="center">
			<p class="text-danger">目前没有符合要求的标</p>
		</td>
	</tr>
</#if>

<script type="text/javascript">
	$(function(){
		$("#page_container").empty().append($('<ul id="pagination" class="pagination"></ul>'));
		$("#pagination").twbsPagination({
			totalPages:${pageResult.totalPages},
			startPage:${pageResult.page},
			initiateStartPageClick:false,
			onPageClick : function(event, page) {
				$("#currentPage").val(page);
				$("#searchForm").submit();
			}
		});
	});
</script>