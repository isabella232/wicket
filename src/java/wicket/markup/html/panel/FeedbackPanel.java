/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.panel;

import wicket.FeedbackMessage;
import wicket.FeedbackMessages;
import wicket.markup.ComponentTagAttributeModifier;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.validation.IValidationErrorHandler;
import wicket.markup.html.table.ListItem;
import wicket.markup.html.table.ListView;
import wicket.model.IModel;

/**
 * A simple panel that displays {@link wicket.FeedbackMessage}s in a list view.
 * The maximum number of messages to show can be set with setMaxMessages().
 * 
 * @see wicket.FeedbackMessage
 * @see wicket.FeedbackMessages
 * @see wicket.markup.html.form.validation.IValidationErrorHandler
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public final class FeedbackPanel extends Panel implements IValidationErrorHandler
{
	/** Serial Version ID. */
	private static final long serialVersionUID = -3385823935971399988L;

	/** The maximum number of messages to show at a time */
	private int maxMessages = Integer.MAX_VALUE;

	/**
	 * List for messages.
	 */
	private static final class MessageListView extends ListView
	{
		/**
		 * @see wicket.Component#Component(String)
		 */
		public MessageListView(String name)
		{
			super(name, FeedbackMessages.model());
		}

		/**
		 * Removes all subcomponents on each render pass, to ensure that the
		 * dynamic model is always read again.
		 * 
		 * @see wicket.Component#handleRender()
		 */
		protected void handleRender()
		{
			super.handleRender();
			removeAll();
		}

		/**
		 * @see wicket.markup.html.table.ListView#populateItem(wicket.markup.html.table.ListItem)
		 */
		protected void populateItem(final ListItem listItem)
		{
			final FeedbackMessage message = (FeedbackMessage)listItem.getModelObject();
			IModel replacementModel = new IModel()
			{

				/**
				 * Returns feedbackPanel + the message level, eg
				 * 'feedbackPanelERROR'. This is used as the class of the li /
				 * span elements.
				 * 
				 * @see wicket.model.IModel#getObject()
				 */
				public Object getObject()
				{
					return "feedbackPanel" + message.getLevelAsString();
				}

				/**
				 * @see wicket.model.IModel#setObject(java.lang.Object)
				 */
				public void setObject(Object object)
				{
				}
			};

			final Label label = new Label("message", message, "message");
			final ComponentTagAttributeModifier levelModifier = new ComponentTagAttributeModifier(
					"class", replacementModel);
			label.add(levelModifier);
			listItem.add(levelModifier);
			listItem.add(label);
		}
	}

	/**
	 * @see wicket.Component#Component(String)
	 */
	public FeedbackPanel(final String componentName)
	{
		super(componentName);
		addComponents();
	}

	/**
	 * @return Returns the maxMessages.
	 */
	public int getMaxMessages()
	{
		return maxMessages;
	}

	/**
	 * @param maxMessages
	 *            The maximum number of feedback messages that this feedback
	 *            panel should show at one time
	 */
	public void setMaxMessages(int maxMessages)
	{
		this.maxMessages = maxMessages;
	}

	/**
	 * Sets an error message to be displayed by this feedback panel.
	 * 
	 * @param errors
	 *            The errors structure
	 * @see wicket.markup.html.form.validation.IValidationErrorHandler#validationError(wicket.FeedbackMessages)
	 */
	public void validationError(final FeedbackMessages errors)
	{
		// Force re-rendering of the list
		removeAll();
		addComponents();
	}

	/**
	 * Adds the components to the panel.
	 */
	private void addComponents()
	{
        final MessageListView view = new MessageListView("messages");
        view.setViewSize(getMaxMessages());
		add(view);
	}
}