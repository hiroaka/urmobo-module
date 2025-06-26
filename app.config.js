// app.config.js
import withUrmoboModule from './plugin/withUrmobo';

export default {
  name: 'UrmoboModule',
  // ... other config
  plugins: [
    withUrmoboModule,
    // ... other plugins
  ],
};