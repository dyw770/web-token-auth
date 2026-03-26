package cn.dyw.engine.core.exec.result;

import cn.dyw.engine.core.context.SelectExecContext;
import lombok.Getter;

/**
 * @author dyw770
 * @since 2022-10-25
 */
public class PageDataSetResult extends DataSetResult {

    @Getter
    public final long total;

    public PageDataSetResult(DataSetResult result, long total) {
        super(result.getContext());
        this.addData(result.getData());
        this.initSchema(result.getSchema());
        this.total = total;
    }

    public PageDataSetResult(SelectExecContext context, long total) {
        super(context);
        this.total = total;
    }

    public int getPage() {
        return getContext().getDataPageOption().getPage();
    }

    public int getSize() {
        return getContext().getDataPageOption().getSize();
    }

    @Override
    public String toString() {
        return "Page{" +
                "total=" + total +
                ",page=" + getPage() +
                ",size=" + getSize() +
                '}' + "\n"
                + super.toString();
    }
}
