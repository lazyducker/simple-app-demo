package net.simpleframework.demo;

import java.util.Iterator;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.lib.org.jsoup.nodes.Document;
import net.simpleframework.lib.org.jsoup.nodes.Element;
import net.simpleframework.module.news.INewsContextAware;
import net.simpleframework.module.news.News;
import net.simpleframework.module.news.web.INewsWebContext;
import net.simpleframework.module.news.web.NewsPageletCreator;
import net.simpleframework.module.news.web.NewsUrlsFactory;
import net.simpleframework.module.news.web.page.NewsViewTPage;
import net.simpleframework.mvc.PageMapping;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.imageslide.ImageItem;
import net.simpleframework.mvc.component.ui.imageslide.ImageItems;
import net.simpleframework.mvc.template.struct.ListRows;
import net.simpleframework.mvc.template.t2.HomeTemplatePage;

@PageMapping(url = "/home")
public class HomePage extends HomeTemplatePage implements INewsContextAware {

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		pp.addImportCSS(HomePage.class, "/home.css");
	}

	@Override
	protected ListRows getListRows(final PageParameter pp, final int row, final int col) {
		// if (row == 1 && col == 1) {
		// if (category != null) {
		final NewsPageletCreator creator = ((INewsWebContext) newsContext).getPageletCreator();
		return creator.create(pp, _newsService.queryContentBeans("集团要闻"));
		// }
		// }
		// return super.toPageletHTML(pp, row, col);
	}

	@Override
	protected ImageItems getImageItems(final ComponentParameter cp) {
		final IDataQuery<?> dq = _newsService.queryImageNews(null);
		final NewsUrlsFactory uFactory = ((INewsWebContext) newsContext).getUrlsFactory();
		final ImageItems items = ImageItems.of();
		for (News news; (news = (News) dq.next()) != null;) {
			final Document doc = HtmlUtils.createHtmlDocument(news.getContent());
			final Iterator<Element> it = doc.select("img[src]").iterator();
			Element ele;
			if (it.hasNext() && (ele = it.next()) != null) {
				items.append(new ImageItem(ele.attr("src"), uFactory.getUrl(cp, NewsViewTPage.class,
						news), news.getTopic()));
			}
		}
		return items;
	}
}
